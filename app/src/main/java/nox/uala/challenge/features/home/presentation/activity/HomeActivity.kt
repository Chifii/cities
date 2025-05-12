package nox.uala.challenge.features.home.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import nox.uala.challenge.R
import nox.uala.challenge.app.theme.CitiesTheme
import nox.uala.challenge.features.cityinfo.presentation.screen.CityInfoScreen
import nox.uala.challenge.features.home.domain.model.City
import nox.uala.challenge.features.home.presentation.layout.DisplayType
import nox.uala.challenge.features.home.presentation.layout.SplitScreenLayout
import nox.uala.challenge.features.home.presentation.layout.detectDisplayType
import nox.uala.challenge.features.home.presentation.screen.HomeScreen
import nox.uala.challenge.features.home.presentation.viewmodel.HomeViewModel
import nox.uala.challenge.features.map.presentation.screen.MapScreen
import nox.uala.challenge.app.navigation.NavGraph
import nox.uala.challenge.app.navigation.NavigationRoutes

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CitiesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val displayType = detectDisplayType()

                    if (displayType == DisplayType.PORTRAIT) {
                        NavGraph(
                            navController = rememberNavController(),
                            startDestination = NavigationRoutes.HOME_ROUTE,
                        )
                    } else {
                        LandscapeLayout()
                    }
                }
            }
        }
    }
}

@Composable
fun LandscapeLayout() {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    var selectedCity by remember { mutableStateOf<City?>(null) }
    var showDetailedInfo by remember { mutableStateOf(false) }

    SplitScreenLayout(
        leftContent = {
            HomeScreen(
                viewModel = homeViewModel,
                onCityClick = { city ->
                    selectedCity = city
                    showDetailedInfo = false
                },
                onInfoClick = { city ->
                    selectedCity = city
                    showDetailedInfo = true
                },
            )
        },
        rightContent = {
            selectedCity?.let { city ->
                if (showDetailedInfo) {
                    CityInfoScreen(
                        city = city,
                        onBackPressed = { showDetailedInfo = false }
                    )
                } else {
                    MapScreen(
                        city = city,
                        onBackPressed = { /* No se necesita en modo landscape */ }
                    )
                }
            } ?: Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.initial_map_text),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    )
}
