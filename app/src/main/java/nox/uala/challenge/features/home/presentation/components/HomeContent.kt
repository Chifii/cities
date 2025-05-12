package nox.uala.challenge.features.home.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nox.uala.challenge.features.home.domain.model.City

@Composable
fun HomeContent(
    modifier: Modifier,
    cities: List<City>,
    searchQuery: String,
    showOnlyFavorites: Boolean,
    error: String?,
    onSearchQueryChanged: (String) -> Unit,
    onToggleFavoriteFilter: (Boolean) -> Unit,
    onToggleFavorite: (Int, Boolean) -> Unit,
    onCityClick: (City) -> Unit,
    onInfoClick: (City) -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChanged,
            showOnlyFavorites = showOnlyFavorites,
            onToggleFavoriteFilter = onToggleFavoriteFilter
        )

        error?.let { errorMessage ->
            ErrorCard(
                errorMessage = errorMessage,
                onRetry = onRetry
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            CityList(
                cities = cities,
                onToggleFavorite = onToggleFavorite,
                onCityClick = onCityClick,
                onInfoClick = onInfoClick
            )
        }
    }
}
