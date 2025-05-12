package nox.uala.challenge.features.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nox.uala.challenge.core.util.AppLogger
import nox.uala.challenge.core.util.ErrorMapper.mapExceptionToAppError
import nox.uala.challenge.core.util.ErrorMapper.mapResourceErrorToAppError
import nox.uala.challenge.core.util.retryWithExponentialBackoff
import nox.uala.challenge.features.home.domain.usecase.FilterCitiesUseCase
import nox.uala.challenge.features.home.domain.usecase.GetCitiesUseCase
import nox.uala.challenge.features.home.domain.usecase.ToggleFavoriteCityUseCase
import nox.uala.challenge.features.home.domain.util.Resource
import nox.uala.challenge.features.home.presentation.screen.HomeUiState
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val toggleFavoriteCityUseCase: ToggleFavoriteCityUseCase,
    private val filterCitiesUseCase: FilterCitiesUseCase,
) : IHomeViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    override val uiState: StateFlow<HomeUiState> = _uiState

    private val _searchQuery = MutableStateFlow("")
    override val searchQuery = _searchQuery.asStateFlow()

    private val _citiesLoaded = MutableStateFlow(false)
    private val _isSearching = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            delay(100)
            loadCities()

            // Flujo para UI inmediata
            launch {
                _searchQuery.collect { query ->
                    _isSearching.value = true
                    AppLogger.d("Query cambiada: $query")

                    if (_citiesLoaded.value && _uiState.value is HomeUiState.Success) {
                        val currentState = _uiState.value as HomeUiState.Success
                        _uiState.value = currentState.copy(searchQuery = query)
                    }
                }
            }

            // Flujo con debounce
            launch {
                _searchQuery
                    .debounce(300)
                    .distinctUntilChanged()
                    .collect { query ->
                        AppLogger.d("Aplicando filtro después de debounce: $query")
                        if (_citiesLoaded.value && _uiState.value is HomeUiState.Success) {
                            val currentState = _uiState.value as HomeUiState.Success
                            updateSuccessState(currentState.copy(searchQuery = query))
                            _isSearching.value = false
                        }
                    }
            }
        }
    }

    override fun loadCities() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            AppLogger.i("Cargando ciudades...")

            try {
                val result = retryWithExponentialBackoff {
                    getCitiesUseCase()
                }

                when (result) {
                    is Resource.Success -> {
                        result.data?.let { cities ->
                            AppLogger.i("Ciudades cargadas con éxito: ${cities.size}")
                            val currentShowOnlyFavorites =
                                (_uiState.value as? HomeUiState.Success)?.showOnlyFavorites == true
                            val currentQuery = _searchQuery.value

                            val filteredCities = filterCitiesUseCase.filterInMemory(
                                cities = cities,
                                searchQuery = currentQuery,
                                showOnlyFavorites = currentShowOnlyFavorites
                            )

                            val noResults = filteredCities.isEmpty() &&
                                    (currentQuery.isNotEmpty() || currentShowOnlyFavorites)

                            _uiState.value = HomeUiState.Success(
                                allCities = cities,
                                filteredCities = filteredCities,
                                searchQuery = currentQuery,
                                showOnlyFavorites = currentShowOnlyFavorites,
                                noSearchResults = noResults
                            )
                        }
                        _citiesLoaded.value = true
                    }

                    is Resource.Error -> {
                        val error = mapResourceErrorToAppError(result)
                        AppLogger.e(error)
                        _uiState.value = HomeUiState.Error(error)
                        _citiesLoaded.value = false
                    }

                    is Resource.Loading -> { /* Estado ya manejado */
                    }
                }
            } catch (e: Exception) {
                val appError = mapExceptionToAppError(e)
                AppLogger.e(appError)
                _uiState.value = HomeUiState.Error(appError)
            }
        }
    }

    override fun updateSearchQuery(query: String) {
        val limitedQuery = if (query.length > 20) query.substring(0, 20) else query
        _searchQuery.value = limitedQuery

        if (_uiState.value is HomeUiState.Success) {
            val currentState = _uiState.value as HomeUiState.Success
            _uiState.value = currentState.copy(searchQuery = limitedQuery)
        }
    }

    override fun toggleFavoriteFilter(showOnlyFavorites: Boolean) {
        if (_uiState.value is HomeUiState.Success) {
            val currentState = _uiState.value as HomeUiState.Success
            if (currentState.showOnlyFavorites != showOnlyFavorites) {
                updateSuccessState(currentState.copy(showOnlyFavorites = showOnlyFavorites))
            }
        }
    }

    override fun toggleFavorite(cityId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            val previousState = _uiState.value
            _uiState.value = HomeUiState.Loading
            AppLogger.d("Cambiando favorito para ciudad $cityId a $isFavorite")

            try {
                val toggleResult = retryWithExponentialBackoff {
                    withContext(Dispatchers.IO) {
                        toggleFavoriteCityUseCase(cityId, isFavorite)
                    }
                }

                if (toggleResult is Resource.Success) {
                    AppLogger.i("Favorito actualizado correctamente para ciudad $cityId")

                    val citiesResult = retryWithExponentialBackoff {
                        withContext(Dispatchers.IO) {
                            getCitiesUseCase()
                        }
                    }

                    when (citiesResult) {
                        is Resource.Success -> {
                            citiesResult.data?.let { cities ->
                                val currentState = previousState as? HomeUiState.Success
                                    ?: HomeUiState.Success()
                                updateSuccessState(currentState.copy(allCities = cities))
                            }
                        }

                        is Resource.Error -> {
                            val error = mapResourceErrorToAppError(citiesResult)
                            AppLogger.e("Error al recargar ciudades tras cambiar favorito")
                            AppLogger.e(error)
                            _uiState.value = HomeUiState.Error(error)
                        }

                        is Resource.Loading -> { /* Ya manejado */
                        }
                    }
                } else if (toggleResult is Resource.Error) {
                    val error = mapResourceErrorToAppError(toggleResult)
                    AppLogger.e(error)
                    _uiState.value = HomeUiState.Error(error)
                }
            } catch (e: Exception) {
                val appError = mapExceptionToAppError(e)
                AppLogger.e(appError)
                _uiState.value = HomeUiState.Error(appError)
            }
        }
    }

    private fun updateSuccessState(state: HomeUiState.Success) {
        viewModelScope.launch {
            val filteredCities = filterCitiesUseCase.filterInMemory(
                cities = state.allCities,
                searchQuery = state.searchQuery,
                showOnlyFavorites = state.showOnlyFavorites
            )

            val noResults = filteredCities.isEmpty() &&
                    (state.searchQuery.isNotEmpty() || state.showOnlyFavorites)

            _uiState.value = state.copy(
                filteredCities = filteredCities,
                noSearchResults = noResults
            )
        }
    }
}