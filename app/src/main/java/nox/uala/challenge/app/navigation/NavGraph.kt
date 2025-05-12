package nox.uala.challenge.app.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import nox.uala.challenge.features.cityinfo.presentation.screen.CityInfoScreen
import nox.uala.challenge.features.home.domain.model.City
import nox.uala.challenge.features.home.presentation.screen.HomeScreen
import nox.uala.challenge.features.home.presentation.viewmodel.HomeViewModel
import nox.uala.challenge.features.map.presentation.screen.MapScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = NavigationRoutes.HOME_ROUTE,
) {
    val navigationActions = remember(navController) {
        NavigationActions(navController)
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        addHomeScreen(navigationActions)
        addMapScreen(navigationActions)
        addCityInfoScreen(navigationActions)
    }
}

private fun NavGraphBuilder.addHomeScreen(
    navigationActions: NavigationActions
) {
    composable(NavigationRoutes.HOME_ROUTE) {
        HomeScreen(
            viewModel = hiltViewModel<HomeViewModel>(),
            onCityClick = { city ->
                navigationActions.navigateToMap(city)
            },
            onInfoClick = { city ->
                navigationActions.navigateToCityInfo(city)
            }
        )
    }
}

private fun NavGraphBuilder.addMapScreen(
    navigationActions: NavigationActions
) {
    composable(
        route = "${NavigationRoutes.MAP_ROUTE}/{cityJson}",
        arguments = listOf(
            navArgument("cityJson") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val cityJson = backStackEntry.arguments?.getString("cityJson")
        val city = cityJson?.let {
            try {
                Gson().fromJson(Uri.decode(it), City::class.java)
            } catch (e: Exception) {
                null
            }
        }

        if (city != null) {
            MapScreen(
                city = city,
                onBackPressed = navigationActions::navigateBack
            )
        } else {
            LaunchedEffect(key1 = Unit) {
                navigationActions.navigateBack()
            }
        }
    }
}

private fun NavGraphBuilder.addCityInfoScreen(
    navigationActions: NavigationActions
) {
    composable(
        route = "${NavigationRoutes.CITY_INFO_ROUTE}/{cityJson}",
        arguments = listOf(
            navArgument("cityJson") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val cityJson = backStackEntry.arguments?.getString("cityJson")
        val city = cityJson?.let {
            try {
                Gson().fromJson(Uri.decode(it), City::class.java)
            } catch (e: Exception) {
                null
            }
        }

        if (city != null) {
            CityInfoScreen(
                city = city,
                onBackPressed = navigationActions::navigateBack
            )
        } else {
            LaunchedEffect(key1 = Unit) {
                navigationActions.navigateBack()
            }
        }
    }
}