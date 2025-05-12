package nox.uala.challenge.app.navigation

/**
 * Objeto que contiene todas las rutas de navegación de la aplicación
 */
object NavigationRoutes {
    const val HOME_ROUTE = "home"
    const val MAP_ROUTE = "map"
    const val CITY_INFO_ROUTE = "city_info"
    
    // Rutas completas con parámetros
    fun mapRoute(lat: Double, lng: Double): String = "$MAP_ROUTE/$lat/$lng"
    fun cityInfoRoute(cityId: Int): String = "$CITY_INFO_ROUTE/$cityId"
}
