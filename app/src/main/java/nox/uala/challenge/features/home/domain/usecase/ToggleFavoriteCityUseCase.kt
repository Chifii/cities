package nox.uala.challenge.features.home.domain.usecase

import nox.uala.challenge.features.home.domain.repository.HomeRepository
import nox.uala.challenge.features.home.domain.util.Resource
import javax.inject.Inject

class ToggleFavoriteCityUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(cityId: Int, isFavorite: Boolean): Resource<Unit> {
        return try {
            repository.toggleFavorite(cityId, isFavorite)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error al cambiar estado de favorito: ${e.localizedMessage}")
        }
    }
}
