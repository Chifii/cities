package nox.uala.challenge.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import nox.uala.challenge.features.home.presentation.screen.HomeUiState

abstract class IHomeViewModel : ViewModel() {
    abstract val uiState: StateFlow<HomeUiState>
    abstract val searchQuery: StateFlow<String>

    abstract fun loadCities()
    abstract fun updateSearchQuery(query: String)
    abstract fun toggleFavoriteFilter(showOnlyFavorites: Boolean)
    abstract fun toggleFavorite(cityId: Int, isFavorite: Boolean)
}