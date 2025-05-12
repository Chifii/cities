package nox.uala.challenge.features.home.domain.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import nox.uala.challenge.features.home.domain.model.City
import nox.uala.challenge.features.home.domain.repository.HomeRepository
import nox.uala.challenge.features.home.domain.util.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FilterCitiesUseCaseTest {

    private lateinit var repository: HomeRepository
    private lateinit var filterCitiesUseCase: FilterCitiesUseCase

    private val testCities = listOf(
        City(1, "Alabama", "US", 0.0, 0.0, false),
        City(2, "Albuquerque", "US", 0.0, 0.0, false),
        City(3, "Anaheim", "US", 0.0, 0.0, false),
        City(4, "Arizona", "US", 0.0, 0.0, false),
        City(5, "Sydney", "AU", 0.0, 0.0, false)
    )

    @Before
    fun setUp() {
        repository = mock()
        filterCitiesUseCase = FilterCitiesUseCase(repository)
    }

    @Test
    fun `when prefix is A should return all cities starting with A`() = runTest {
        // Given
        val prefix = "A"
        val expectedCities = testCities.filter { it.name.startsWith(prefix, ignoreCase = true) }
        whenever(
            repository.getCitiesByPrefix(
                prefix, false
            )
        ).thenReturn(flowOf(expectedCities).first())

        // When
        val result = filterCitiesUseCase(prefix, false)

        // Then
        assertTrue(result is Resource.Success)
        val cities = (result as Resource.Success).data ?: emptyList()
        assertEquals(4, cities.size)
        assertTrue(cities.any { it.name == "Alabama" })
        assertTrue(cities.any { it.name == "Albuquerque" })
        assertTrue(cities.any { it.name == "Anaheim" })
        assertTrue(cities.any { it.name == "Arizona" })
    }

    @Test
    fun `when prefix is s should return only Sydney`() = runTest {
        // Given
        val prefix = "s"
        val expectedCities = listOf(testCities[4]) // Sydney
        whenever(
            repository.getCitiesByPrefix(
                prefix, false
            )
        ).thenReturn(flowOf(expectedCities).first())

        // When
        val result = filterCitiesUseCase(prefix, false)

        // Then
        assertTrue(result is Resource.Success)
        val cities = (result as Resource.Success).data ?: emptyList()
        assertEquals(1, cities.size)
        assertEquals("Sydney", cities[0].name)
    }

    @Test
    fun `when prefix is Al should return Alabama and Albuquerque`() = runTest {
        // Given
        val prefix = "Al"
        val expectedCities = testCities.filter { it.name.startsWith(prefix, ignoreCase = true) }
        whenever(
            repository.getCitiesByPrefix(
                prefix, false
            )
        ).thenReturn(flowOf(expectedCities).first())

        // When
        val result = filterCitiesUseCase(prefix, false)

        // Then
        assertTrue(result is Resource.Success)
        val cities = (result as Resource.Success).data ?: emptyList()
        assertEquals(2, cities.size)
        assertTrue(cities.any { it.name == "Alabama" })
        assertTrue(cities.any { it.name == "Albuquerque" })
    }

    @Test
    fun `when prefix is Alb should return only Albuquerque`() = runTest {
        // Given
        val prefix = "Alb"
        val expectedCities = listOf(testCities[1]) // Albuquerque
        whenever(
            repository.getCitiesByPrefix(
                prefix, false
            )
        ).thenReturn(flowOf(expectedCities).first())

        // When
        val result = filterCitiesUseCase(prefix, false)

        // Then
        assertTrue(result is Resource.Success)
        val cities = (result as Resource.Success).data ?: emptyList()
        assertEquals(1, cities.size)
        assertEquals("Albuquerque", cities[0].name)
    }

    @Test
    fun `when search string is empty should return all cities`() = runTest {
        // Given
        whenever(repository.getAllCities()).thenReturn(flowOf(testCities).first())

        // When
        val result = filterCitiesUseCase("", false)

        // Then
        assertTrue(result is Resource.Success)
        val cities = (result as Resource.Success).data ?: emptyList()
        assertEquals(testCities.size, cities.size)
    }

    @Test
    fun `when invalid search string should return empty list`() = runTest {
        // Given
        val prefix = "XYZ"
        whenever(
            repository.getCitiesByPrefix(
                prefix, false
            )
        ).thenReturn(flowOf(emptyList<City>()).first())

        // When
        val result = filterCitiesUseCase(prefix, false)

        // Then
        assertTrue(result is Resource.Success)
        val cities = (result as Resource.Success).data ?: emptyList()
        assertTrue(cities.isEmpty())
    }

    @Test
    fun `when error occurs should return Resource Error`() = runTest {
        // Given
        val prefix = "A"
        val errorMessage = "Database error"
        whenever(repository.getCitiesByPrefix(prefix, false)).thenThrow(
            RuntimeException(
                errorMessage
            )
        )

        // When
        val result = filterCitiesUseCase(prefix, false)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Error al filtrar ciudades: $errorMessage", (result as Resource.Error).message)
    }

    @Test
    fun `when prefix is mixed case should be case insensitive`() = runTest {
        // Given
        val prefix = "aLaBaMa"
        val expectedCities = listOf(testCities[0]) // Alabama
        whenever(
            repository.getCitiesByPrefix(
                prefix, false
            )
        ).thenReturn(flowOf(expectedCities).first())

        // When
        val result = filterCitiesUseCase(prefix, false)

        // Then
        assertTrue(result is Resource.Success)
        val cities = (result as Resource.Success).data ?: emptyList()
        assertEquals(1, cities.size)
        assertEquals("Alabama", cities[0].name)
    }

    @Test
    fun `when only favorites is true should filter only favorite cities`() = runTest {
        // Given
        val favoriteCities = listOf(
            testCities[0].copy(isFavorite = true), testCities[2].copy(isFavorite = true)
        )
        whenever(repository.getFavoriteCities()).thenReturn(flowOf(favoriteCities).first())

        // When
        val result = filterCitiesUseCase("", true)

        // Then
        assertTrue(result is Resource.Success)
        val cities = (result as Resource.Success).data ?: emptyList()
        assertEquals(2, cities.size)
        assertTrue(cities.all { it.isFavorite })
    }

    /*@Test
    fun `when prefix and onlyFavorites are provided should filter by both criteria`() = runTest {
        // Given
        val prefix = "A"
        val favoriteCities = listOf(
            testCities[0].copy(isFavorite = true), // Alabama
            testCities[3].copy(isFavorite = true)  // Arizona
        )
        whenever(repository.getCitiesByPrefixAndFavorite(prefix))
            .thenReturn(flowOf(favoriteCities).first())

        // When
        val result = filterCitiesUseCase(prefix, true)

        // Then
        assertTrue(result is Resource.Success)
        val cities = (result as Resource.Success).data ?: emptyList()
        assertEquals(2, cities.size)
        assertTrue(cities.all { it.name.startsWith("A", ignoreCase = true) && it.isFavorite })
    }

    @Test
    fun `when repository returns empty list should return empty success`() = runTest {
        // Given
        val prefix = "Z" // No cities start with Z
        whenever(repository.getCitiesByPrefix(prefix, false))
            .thenReturn(flowOf(emptyList()).first())

        // When
        val result = filterCitiesUseCase(prefix, false)

        // Then
        assertTrue(result is Resource.Success)
        val cities = (result as Resource.Success).data ?: emptyList()
        assertTrue(cities.isEmpty())
    }

    @Test
    fun `should call correct repository method based on parameters`() = runTest {
        // Given - empty prefix, no favorites
        whenever(repository.getAllCities()).thenReturn(flowOf(testCities).first())
        
        // When
        filterCitiesUseCase("", false)
        
        // Then
        verify(repository).getAllCities()
        
        // Given - with prefix, no favorites
        val prefix = "A"
        whenever(repository.getCitiesByPrefix(prefix, false))
            .thenReturn(flowOf(emptyList()).first())
            
        // When
        filterCitiesUseCase(prefix, false)
        
        // Then
        verify(repository).getCitiesByPrefix(prefix, false)
        
        // Given - empty prefix, only favorites
        whenever(repository.getFavoriteCities())
            .thenReturn(flowOf(emptyList()).first())
            
        // When
        filterCitiesUseCase("", true)
        
        // Then
        verify(repository).getFavoriteCities()
        
        // Given - with prefix, only favorites
        whenever(repository.getCitiesByPrefixAndFavorite(prefix))
            .thenReturn(flowOf(emptyList()).first())
            
        // When
        filterCitiesUseCase(prefix, true)
        
        // Then
        verify(repository).getCitiesByPrefixAndFavorite(prefix)
    }*/
}
