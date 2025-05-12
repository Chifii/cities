package nox.uala.challenge.features.home.presentation.screen

import nox.uala.challenge.core.exception.AppError
import nox.uala.challenge.features.home.domain.model.City

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Error(val error: AppError) : HomeUiState()
    data class Success(
        val allCities: List<City> = emptyList(),
        val filteredCities: List<City> = emptyList(),
        val searchQuery: String = "",
        val showOnlyFavorites: Boolean = false,
        val noSearchResults: Boolean = false
    ) : HomeUiState()
}