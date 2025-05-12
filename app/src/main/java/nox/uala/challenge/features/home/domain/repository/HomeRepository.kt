package nox.uala.challenge.features.home.domain.repository

import nox.uala.challenge.features.home.domain.model.City

interface HomeRepository {
    suspend fun getCities(): List<City>
    suspend fun getAllCities(): List<City>
    suspend fun getFavoriteCities(): List<City>
    suspend fun getCitiesByPrefix(prefix: String, onlyFavorites: Boolean): List<City>
    suspend fun toggleFavorite(cityId: Int, isFavorite: Boolean)
    suspend fun refreshCities(): Boolean
}
