package nox.uala.challenge.features.home.domain.usecase

import nox.uala.challenge.features.home.domain.model.City
import nox.uala.challenge.features.home.domain.repository.HomeRepository
import nox.uala.challenge.features.home.domain.util.Resource
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Resource<List<City>> {
        return try {
            val cities = repository.getCities()
            val sortedCities = cities.sortedWith(compareBy({ it.name }, { it.country }))
            Resource.Success(sortedCities)
        } catch (e: Exception) {
            Resource.Error("No se pudieron cargar las ciudades: ${e.localizedMessage}")
        }
    }
}
