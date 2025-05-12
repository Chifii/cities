package nox.uala.challenge.features.home.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nox.uala.challenge.BuildConfig
import nox.uala.challenge.features.home.data.local.dao.CityDao
import nox.uala.challenge.features.home.data.local.entity.CityEntity
import nox.uala.challenge.features.home.data.service.HomeService
import nox.uala.challenge.features.home.domain.model.City
import nox.uala.challenge.features.home.domain.repository.HomeRepository
import timber.log.Timber
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService,
    private val cityDao: CityDao
) : HomeRepository {

    override suspend fun getCities(): List<City> {
        return withContext(Dispatchers.IO) {
            val cityCount = cityDao.getCityCount()

            if (cityCount == 0) {
                refreshCities()
            }

            getAllCities()
        }
    }

    override suspend fun getAllCities(): List<City> {
        return withContext(Dispatchers.IO) {
            val cities = cityDao.getAllCities()
            if (BuildConfig.DEBUG) {
                Timber.tag("HomeRepository").d("getAllCities: obtenidas ${cities.size} ciudades")
            }

            if (cities.isEmpty()) {
                Timber.tag("HomeRepository").d("Base de datos vacía, intentando refrescar ciudades")
                if (refreshCities()) {
                    val refreshedCities = cityDao.getAllCities()
                    if (BuildConfig.DEBUG) {
                        Timber.tag("HomeRepository")
                            .d("Después de refrescar: ${refreshedCities.size} ciudades")
                    }
                    return@withContext refreshedCities.map { it.toDomainModel() }
                }
            }

            cities.map { it.toDomainModel() }
        }
    }

    override suspend fun getFavoriteCities(): List<City> {
        return withContext(Dispatchers.IO) {
            cityDao.getFavoriteCities().map { it.toDomainModel() }
        }
    }

    override suspend fun getCitiesByPrefix(prefix: String, onlyFavorites: Boolean): List<City> {
        return withContext(Dispatchers.IO) {
            val formattedPrefix = "%${prefix.trim()}%"
            val result = if (onlyFavorites) {
                cityDao.getFavoriteCitiesByPrefix(formattedPrefix)
            } else {
                cityDao.getCitiesByPrefix(formattedPrefix)
            }

            if (result == null) {
                if (BuildConfig.DEBUG) {
                    Timber.tag("HomeRepository")
                        .d("La consulta por '$formattedPrefix' devolvió null")
                }
                return@withContext emptyList()
            }

            if (BuildConfig.DEBUG) {
                Timber.tag("HomeRepository")
                    .d("Consulta por '$formattedPrefix' (favoritos=$onlyFavorites): ${result.size} resultados")
            }

            result?.map { it.toDomainModel() } ?: emptyList()
        }
    }

    override suspend fun toggleFavorite(cityId: Int, isFavorite: Boolean) {
        try {
            withContext(Dispatchers.IO) {
                cityDao.updateFavoriteStatus(cityId, isFavorite)
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Timber.tag("HomeRepository").e(e, "Error al actualizar favorito: ${e.message}")
            }
        }
    }

    override suspend fun refreshCities(): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val citiesDto = homeService.getCities()
                if (BuildConfig.DEBUG) {
                    Timber.tag("HomeRepository")
                        .d("Ciudades recibidas del servicio: ${citiesDto.size}")
                }

                val favoriteIds = cityDao.getFavoriteIds()

                val cityEntities = citiesDto.map { dto ->
                    CityEntity(
                        id = dto.id ?: 0,
                        name = dto.name ?: "",
                        country = dto.country ?: "",
                        latitude = dto.coordinates?.lat ?: 0.0,
                        longitude = dto.coordinates?.lon ?: 0.0,
                        isFavorite = favoriteIds.contains(dto.id)
                    )
                }.distinctBy { it.id }

                if (BuildConfig.DEBUG) {
                    Timber.tag("HomeRepository").d("Ciudades a insertar: ${cityEntities.size}")
                }

                cityDao.insertCities(cityEntities)
                true
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Timber.tag("HomeRepository").e(e, "Error al refrescar ciudades: ${e.message}")
            }
            false
        }
    }
}
