package nox.uala.challenge.features.home.domain.usecase

import nox.uala.challenge.features.home.domain.model.City
import nox.uala.challenge.features.home.domain.repository.HomeRepository
import nox.uala.challenge.features.home.domain.util.Resource
import javax.inject.Inject

class FilterCitiesUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(
        searchQuery: String,
        showOnlyFavorites: Boolean
    ): Resource<List<City>> {
        return try {
            val result = when {
                searchQuery.isEmpty() -> {
                    if (showOnlyFavorites) {
                        repository.getFavoriteCities()
                    } else {
                        repository.getAllCities()
                    }
                }

                else -> {
                    repository.getCitiesByPrefix(searchQuery, showOnlyFavorites)
                }
            }

            Resource.Success(result)
        } catch (e: Exception) {
            return Resource.Error("Error al filtrar ciudades: ${e.message ?: "Error desconocido"}")
        }
    }

    fun filterInMemory(
        cities: List<City>,
        searchQuery: String,
        showOnlyFavorites: Boolean
    ): List<City> {
        return cities.filter { city ->
            val matchesQuery = searchQuery.isEmpty() ||
                    city.name.startsWith(searchQuery, ignoreCase = true)
            val matchesFavorite = !showOnlyFavorites || city.isFavorite
            matchesQuery && matchesFavorite
        }
    }
}