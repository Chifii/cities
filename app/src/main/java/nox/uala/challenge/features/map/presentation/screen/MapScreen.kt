package nox.uala.challenge.features.map.presentation.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nox.uala.challenge.features.home.domain.model.City
import nox.uala.challenge.features.home.presentation.layout.DisplayType
import nox.uala.challenge.features.home.presentation.layout.detectDisplayType
import nox.uala.challenge.features.map.presentation.components.MapContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    city: City,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayType = detectDisplayType()

    if (displayType == DisplayType.LANDSCAPE) {
        MapContent(city = city, modifier = modifier)
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("${city.name}, ${city.country}") },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            },
            modifier = modifier
        ) { paddingValues ->
            MapContent(
                city = city,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Preview
@Composable
fun MapScreenPreview() {
    MapScreen(
        city = City(
            id = 1,
            name = "Barcelona",
            country = "ES",
            lat = 41.3851,
            lon = 2.1734,
            isFavorite = true
        ),
        onBackPressed = {}
    )
}
