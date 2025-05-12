package nox.uala.challenge.features.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nox.uala.challenge.features.home.domain.model.City

@Composable
internal fun CityList(
    cities: List<City>,
    onToggleFavorite: (Int, Boolean) -> Unit,
    onCityClick: (City) -> Unit,
    onInfoClick: (City) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = cities,
            key = { it.id }
        ) { city ->
            key(city.id) {
                CityItem(
                    city = city,
                    onToggleFavorite = { onToggleFavorite(city.id, city.isFavorite.not()) },
                    onClick = { onCityClick(city) },
                    onInfoClick = { onInfoClick(city) }
                )
            }
        }
    }
}