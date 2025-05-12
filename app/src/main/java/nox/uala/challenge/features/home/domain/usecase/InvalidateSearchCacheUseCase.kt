package nox.uala.challenge.features.home.domain.usecase

import nox.uala.challenge.features.home.data.repository.CitySearchCache
import javax.inject.Inject

class InvalidateSearchCacheUseCase @Inject constructor(
    private val citySearchCache: CitySearchCache
) {
    operator fun invoke() {
        citySearchCache.clear()
    }
}