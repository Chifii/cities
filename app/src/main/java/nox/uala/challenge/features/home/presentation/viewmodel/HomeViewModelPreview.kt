package nox.uala.challenge.features.home.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import nox.uala.challenge.features.home.domain.model.City
import nox.uala.challenge.features.home.presentation.screen.HomeUiState

class HomeViewModelPreview : IHomeViewModel() {
    override val uiState = MutableStateFlow<HomeUiState>(
        HomeUiState.Success(
            allCities = listOf(
                City(1, "New York", "US", 40.7128, -74.0060, false),
                City(2, "London", "UK", 51.5074, -0.1278, true),
                City(3, "Paris", "FR", 48.8566, 2.3522, false),
                City(4, "Tokyo", "JP", 35.6762, 139.6503, true),
                City(5, "Sydney", "AU", -33.8688, 151.2093, false)
            ),
            filteredCities = listOf(
                City(1, "New York", "US", 40.7128, -74.0060, false),
                City(2, "London", "UK", 51.5074, -0.1278, true)
            )
        )
    )
    override val searchQuery = MutableStateFlow("")

    override fun loadCities() {}
    override fun updateSearchQuery(query: String) {}
    override fun toggleFavoriteFilter(showOnlyFavorites: Boolean) {}
    override fun toggleFavorite(cityId: Int, isFavorite: Boolean) {}
}