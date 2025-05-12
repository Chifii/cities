package nox.uala.challenge.features.home.data.repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import nox.uala.challenge.features.home.data.local.dao.CityDao
import nox.uala.challenge.features.home.data.service.HomeService
import nox.uala.challenge.resources.mockito_extensions.testCityDtos
import nox.uala.challenge.resources.mockito_extensions.testCityEntities
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class HomeRepositoryImplTest {

    private lateinit var homeService: HomeService
    private lateinit var cityDao: CityDao
    private lateinit var repository: HomeRepositoryImpl

    @Before
    fun setup() {
        homeService = mock()
        cityDao = mock()
        repository = HomeRepositoryImpl(homeService, cityDao)
    }

    @Test
    fun `getAllCities returns cities from local database`() = runTest {
        // Given
        whenever(cityDao.getAllCities()).thenReturn(flowOf(testCityEntities).first())

        // When
        val result = repository.getAllCities()

        // Then
        assertEquals(4, result.size)
        assertEquals("New York", result[0].name)
        assertEquals("US", result[0].country)
        assertEquals(40.7128, result[0].lat, 0.0001)
        assertEquals(-74.0060, result[0].lon, 0.0001)
        assertFalse(result[0].isFavorite)
        assertTrue(result[1].isFavorite)
    }

    @Test
    fun `getFavoriteCities returns only favorite cities`() = runTest {
        // Given
        val favoriteCities = testCityEntities.filter { it.isFavorite }
        whenever(cityDao.getFavoriteCities()).thenReturn(flowOf(favoriteCities).first())

        // When
        val result = repository.getFavoriteCities()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.all { it.isFavorite })
        assertEquals("London", result[0].name)
        assertEquals("Tokyo", result[1].name)
    }

    @Test
    fun `getCitiesByPrefix returns cities starting with prefix`() = runTest {
        // Given
        val prefix = "L"
        val filteredCities = testCityEntities.filter {
            it.name.startsWith(prefix, ignoreCase = true)
        }
        whenever(cityDao.getCitiesByPrefix("%$prefix%")).thenReturn(flowOf(filteredCities).first())

        // When
        val result = repository.getCitiesByPrefix(prefix, false)

        // Then
        assertEquals(1, result.size)
        assertEquals("London", result[0].name)
    }

    /*@Test
    fun `toggleFavorite updates favorite status`() = runTest {
        // Given
        val city = City(1, "New York", "US", 40.7128, -74.0060, false)
        whenever(cityDao.updateFavoriteStatus(1, true)).thenReturn(1)
        whenever(cityDao.getCityById(1)).thenReturn(
            testCityEntities[0].copy(isFavorite = true)
        )

        // When
        val result = repository.toggleFavorite(city)

        // Then
        verify(cityDao).updateFavoriteStatus(1, true)
        verify(cityDao).getCityById(1)
        assertTrue(result.isFavorite)
    }*/

    @Test
    fun `refreshCities fetches data from remote and updates local database`() = runTest {
        // Given
        whenever(homeService.getCities()).thenReturn(testCityDtos)
        val favoriteIds = listOf(2, 4)
        whenever(cityDao.getFavoriteIds()).thenReturn(favoriteIds)

        // When
        repository.refreshCities()

        // Then
        verify(homeService).getCities()
        verify(cityDao).getFavoriteIds()
        verify(cityDao).insertCities(any())
    }
}
