package nox.uala.challenge.features.home.data.repository

import android.util.LruCache
import nox.uala.challenge.features.home.domain.model.City
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CitySearchCache @Inject constructor() {
    // Clave compuesta: query + favoritesOnly
    private data class CacheKey(val query: String, val favoritesOnly: Boolean)

    // Caché con máximo de 100 consultas
    private val cache = LruCache<CacheKey, List<City>>(100)

    fun get(query: String, favoritesOnly: Boolean): List<City>? {
        return cache.get(CacheKey(query, favoritesOnly))
    }

    fun put(query: String, favoritesOnly: Boolean, cities: List<City>) {
        cache.put(CacheKey(query, favoritesOnly), cities)
    }

    fun clear() {
        cache.evictAll()
    }
}