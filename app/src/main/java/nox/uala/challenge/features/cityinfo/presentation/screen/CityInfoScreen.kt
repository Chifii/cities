package nox.uala.challenge.features.cityinfo.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nox.uala.challenge.R
import nox.uala.challenge.features.home.domain.model.City

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityInfoScreen(
    city: City,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            R.string.city_title_format,
                            city.name,
                            city.country
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // City coordinates
                InfoCard(
                    title = stringResource(R.string.location),
                    content = stringResource(
                        R.string.location_details,
                        city.lat.toString(),
                        city.lon.toString()
                    )
                )

                // General info
                InfoCard(
                    title = stringResource(R.string.general_info),
                    content = stringResource(R.string.general_info_content)
                )

                // Demographics
                InfoCard(
                    title = stringResource(R.string.demographics),
                    content = stringResource(R.string.demographics_content)
                )

                // Economy
                InfoCard(
                    title = stringResource(R.string.economy),
                    content = stringResource(R.string.economy_content)
                )

                // Transportation
                InfoCard(
                    title = stringResource(R.string.transportation),
                    content = stringResource(R.string.transportation_content)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun InfoCard(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
fun CityInfoScreenPreview() {
    val previewCity = City(
        id = 1,
        name = "Barcelona",
        country = "ES",
        lat = 41.3851,
        lon = 2.1734,
        isFavorite = true
    )

    CityInfoScreen(
        city = previewCity,
        onBackPressed = {}
    )
}

