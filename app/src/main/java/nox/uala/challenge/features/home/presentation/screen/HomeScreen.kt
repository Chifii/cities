package nox.uala.challenge.features.home.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nox.uala.challenge.core.exception.ErrorContent
import nox.uala.challenge.features.home.domain.model.City
import nox.uala.challenge.features.home.presentation.components.HomeContent
import nox.uala.challenge.features.home.presentation.components.LoadingLayout
import nox.uala.challenge.features.home.presentation.viewmodel.HomeViewModelPreview
import nox.uala.challenge.features.home.presentation.viewmodel.IHomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: IHomeViewModel, onCityClick: (City) -> Unit, onInfoClick: (City) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val cities by remember {
        derivedStateOf {
            if (uiState is HomeUiState.Success) (uiState as HomeUiState.Success).filteredCities
            else emptyList()
        }
    }

    val showOnlyFavorites by remember {
        derivedStateOf {
            if (uiState is HomeUiState.Success) (uiState as HomeUiState.Success).showOnlyFavorites
            else false
        }
    }

    val onSearchQueryChanged = remember(viewModel) {
        { query: String -> viewModel.updateSearchQuery(query) }
    }

    val onToggleFavoriteFilter = remember(viewModel) {
        { show: Boolean -> viewModel.toggleFavoriteFilter(show) }
    }

    val onToggleFavorite = remember(viewModel) {
        { id: Int, isFavorite: Boolean -> viewModel.toggleFavorite(id, isFavorite) }
    }

    val onRetry = remember(viewModel) {
        { viewModel.loadCities() }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("¡Cities!") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ),
        )
    }, content = { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is HomeUiState.Error -> {
                    ErrorContent(
                        error = (uiState as HomeUiState.Error).error, onRetry = onRetry
                    )
                }

                is HomeUiState.Loading -> {
                    LoadingLayout()
                }

                is HomeUiState.Success -> {
                    HomeContent(
                        modifier = Modifier.padding(paddingValues),
                        cities = cities,
                        searchQuery = searchQuery,
                        showOnlyFavorites = showOnlyFavorites,
                        error = null,
                        onSearchQueryChanged = onSearchQueryChanged,
                        onToggleFavoriteFilter = onToggleFavoriteFilter,
                        onToggleFavorite = onToggleFavorite,
                        onCityClick = onCityClick,
                        onInfoClick = onInfoClick,
                        onRetry = onRetry
                    )
                }
            }
        }
    })
}

@Preview
@Composable
private fun HomeScreenPreview() {
    val mockViewModel = HomeViewModelPreview()
    HomeScreen(viewModel = mockViewModel, onCityClick = {}, onInfoClick = {})
}