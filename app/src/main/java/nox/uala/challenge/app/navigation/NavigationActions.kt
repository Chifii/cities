package nox.uala.challenge.app.navigation

import android.net.Uri
import androidx.navigation.NavController
import com.google.gson.Gson
import nox.uala.challenge.features.home.domain.model.City
import javax.inject.Inject

/**
 * Clase que encapsula las acciones de navegación de la aplicación
 */
class NavigationActions @Inject constructor(
    private val navController: NavController
) {
    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateToHome() {
        navController.navigate(NavigationRoutes.HOME_ROUTE) {
            popUpTo(NavigationRoutes.HOME_ROUTE) {
                inclusive = true
            }
        }
    }

    fun navigateToMap(city: City) {
        val cityJson = Uri.encode(Gson().toJson(city))
        navController.navigate("${NavigationRoutes.MAP_ROUTE}/$cityJson")
    }

    fun navigateToCityInfo(city: City) {
        val cityJson = Uri.encode(Gson().toJson(city))
        navController.navigate("${NavigationRoutes.CITY_INFO_ROUTE}/$cityJson")
    }
}
