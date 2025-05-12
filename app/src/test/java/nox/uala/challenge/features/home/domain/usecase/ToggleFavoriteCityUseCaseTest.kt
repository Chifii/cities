package nox.uala.challenge.features.home.domain.usecase

import kotlinx.coroutines.test.runTest
import nox.uala.challenge.features.home.domain.model.City
import nox.uala.challenge.features.home.domain.repository.HomeRepository
import nox.uala.challenge.features.home.domain.util.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ToggleFavoriteCityUseCaseTest {

    /*private lateinit var repository: HomeRepository
    private lateinit var toggleFavoriteCityUseCase: ToggleFavoriteCityUseCase

    private val testCity = City(
        id = 1,
        name = "New York",
        country = "US",
        lat = 40.7128,
        lng = -74.0060,
        isFavorite = false
    )

    @Before
    fun setUp() {
        repository = mock()
        toggleFavoriteCityUseCase = ToggleFavoriteCityUseCase(repository)
    }

    @Test
    fun `toggleFavorite changes favorite status to true when false`() = runTest {
        // Given
        val city = testCity.copy(isFavorite = false)
        val updatedCity = city.copy(isFavorite = true)
        whenever(repository.toggleFavorite(city)).thenReturn(updatedCity)

        // When
        val result = toggleFavoriteCityUseCase(city)

        // Then
        assertTrue(result is Resource.Success)
        val resultCity = (result as Resource.Success).data
        assertEquals(updatedCity, resultCity)
        assertTrue(resultCity?.isFavorite == true)
        verify(repository).toggleFavorite(city)
    }

    @Test
    fun `toggleFavorite changes favorite status to false when true`() = runTest {
        // Given
        val city = testCity.copy(isFavorite = true)
        val updatedCity = city.copy(isFavorite = false)
        whenever(repository.toggleFavorite(city)).thenReturn(updatedCity)

        // When
        val result = toggleFavoriteCityUseCase(city)

        // Then
        assertTrue(result is Resource.Success)
        val resultCity = (result as Resource.Success).data
        assertEquals(updatedCity, resultCity)
        assertFalse(resultCity?.isFavorite == true)
        verify(repository).toggleFavorite(city)
    }

    @Test
    fun `toggleFavorite returns error when repository throws exception`() = runTest {
        // Given
        val errorMessage = "Database error"
        whenever(repository.toggleFavorite(testCity)).thenThrow(RuntimeException(errorMessage))

        // When
        val result = toggleFavoriteCityUseCase(testCity)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Error al cambiar estado de favorito: $errorMessage", (result as Resource.Error).message)
    }*/
}
