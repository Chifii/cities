package nox.uala.challenge.features.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nox.uala.challenge.features.home.domain.usecase.FilterCitiesUseCase
import nox.uala.challenge.features.home.domain.usecase.GetCitiesUseCase
import nox.uala.challenge.features.home.domain.usecase.ToggleFavoriteCityUseCase
import nox.uala.challenge.features.home.domain.util.Resource
import nox.uala.challenge.resources.mockito_extensions.testCities
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    /*@get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var getCitiesUseCase: GetCitiesUseCase

    @Mock
    private lateinit var filterCitiesUseCase: FilterCitiesUseCase

    @Mock
    private lateinit var toggleFavoriteCityUseCase: ToggleFavoriteCityUseCase

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        runTest {
            `when`(getCitiesUseCase()).thenReturn(Resource.Success(testCities))
            whenever(filterCitiesUseCase(any(), any())).thenAnswer { invocation ->
                val prefix = invocation.arguments[0] as String
                val onlyFavorites = invocation.arguments[1] as Boolean

                var filteredList = testCities

                if (prefix.isNotEmpty()) {
                    filteredList = filteredList.filter {
                        it.name.startsWith(prefix, ignoreCase = true)
                    }
                }

                if (onlyFavorites) {
                    filteredList = filteredList.filter { it.isFavorite }
                }

                Resource.Success(filteredList)
            }
        }

        viewModel = HomeViewModel(
            getCitiesUseCase = getCitiesUseCase,
            filterCitiesUseCase = filterCitiesUseCase,
            toggleFavoriteCityUseCase = toggleFavoriteCityUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `filtrado inicial muestra todas las ciudades`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        val cities = viewModel.filteredCities.first()
        assertEquals(testCities.size, cities.size)
    }

    @Test
    fun `filtrado por prefijo 'A' muestra solo Amsterdam`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.updateSearchQuery("A")
        testDispatcher.scheduler.advanceUntilIdle()

        val filteredCities = viewModel.filteredCities.first()
        assertEquals(1, filteredCities.size)
        assertEquals("Amsterdam", filteredCities[0].name)
    }

    @Test
    fun `filtrado por prefijo 'ne' muestra New York`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.updateSearchQuery("ne")
        testDispatcher.scheduler.advanceUntilIdle()

        val filteredCities = viewModel.filteredCities.first()
        assertEquals(1, filteredCities.size)
        assertEquals("New York", filteredCities[0].name)
    }

    @Test
    fun `búsqueda case insensitive funciona correctamente`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.updateSearchQuery("london")
        testDispatcher.scheduler.advanceUntilIdle()

        val filteredCities = viewModel.filteredCities.first()
        assertEquals(1, filteredCities.size)
        assertEquals("London", filteredCities[0].name)
    }

    @Test
    fun `filtrado por prefijo vacío muestra todas las ciudades`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.updateSearchQuery("")
        testDispatcher.scheduler.advanceUntilIdle()

        val filteredCities = viewModel.filteredCities.first()
        assertEquals(testCities.size, filteredCities.size)
    }

    @Test
    fun `filtrado solo favoritos muestra solo ciudades favoritas`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.toggleFavoriteFilter(true)
        testDispatcher.scheduler.advanceUntilIdle()

        val filteredCities = viewModel.filteredCities.first()
        assertEquals(3, filteredCities.size)
        assertTrue(filteredCities.all { it.isFavorite })
    }

    @Test
    fun `filtrado por prefijo y favoritos funciona correctamente`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.updateSearchQuery("B")
        viewModel.toggleFavoriteFilter(true)
        testDispatcher.scheduler.advanceUntilIdle()

        val filteredCities = viewModel.filteredCities.first()
        assertEquals(1, filteredCities.size)
        assertEquals("Berlin", filteredCities[0].name)
        assertTrue(filteredCities[0].isFavorite)
    }

    @Test
    fun `filtrado con prefijo que no coincide devuelve lista vacía`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.updateSearchQuery("XYZ")
        testDispatcher.scheduler.advanceUntilIdle()

        val filteredCities = viewModel.filteredCities.first()
        assertTrue(filteredCities.isEmpty())
    }

    @Test
    fun `cuando ocurre un error muestra mensaje de error`() = runTest {
        val errorMessage = "Failed to load cities"
        `when`(getCitiesUseCase()).thenReturn(Resource.Error(errorMessage))

        viewModel = HomeViewModel(
            getCitiesUseCase = getCitiesUseCase,
            filterCitiesUseCase = filterCitiesUseCase,
            toggleFavoriteCityUseCase = toggleFavoriteCityUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        val error = viewModel.error.first()
        assertEquals(errorMessage, error)
    }

    @Test
    fun `loadCities llama a getCitiesUseCase`() = runTest {
        // Given
        testDispatcher.scheduler.advanceUntilIdle()
        
        // When
        viewModel.loadCities()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        verify(getCitiesUseCase, times(2)).invoke() // Una vez en init y otra en loadCities
    }

    @Test
    fun `toggleFavorite llama a toggleFavoriteCityUseCase`() = runTest {
        // Given
        val city = testCities[0]
        val toggledCity = city.copy(isFavorite = !city.isFavorite)
        `when`(toggleFavoriteCityUseCase(city)).thenReturn(Resource.Success(toggledCity))
        testDispatcher.scheduler.advanceUntilIdle()
        
        // When
        viewModel.toggleFavorite(city)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        verify(toggleFavoriteCityUseCase).invoke(city)
    }

    @Test
    fun `isLoading se actualiza correctamente durante la carga`() = runTest {
        // Given
        var loadingState = false
        viewModel.isLoading.collect {
            loadingState = it
        }
        
        // When - antes de cargar
        assertFalse(loadingState)
        
        // When - durante la carga
        viewModel.loadCities()
        assertTrue(loadingState)
        
        // When - después de cargar
        testDispatcher.scheduler.advanceUntilIdle()
        assertFalse(loadingState)
    }

    @Test
    fun `filtrado funciona con búsqueda parcial en medio de palabra`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // Configura el caso de uso para buscar en cualquier parte de la palabra
        whenever(filterCitiesUseCase(any(), any())).thenAnswer { invocation ->
            val prefix = invocation.arguments[0] as String
            val onlyFavorites = invocation.arguments[1] as Boolean

            var filteredList = testCities

            if (prefix.isNotEmpty()) {
                filteredList = filteredList.filter {
                    it.name.contains(prefix, ignoreCase = true)
                }
            }

            if (onlyFavorites) {
                filteredList = filteredList.filter { it.isFavorite }
            }

            Resource.Success(filteredList)
        }

        viewModel.updateSearchQuery("er")
        testDispatcher.scheduler.advanceUntilIdle()

        val filteredCities = viewModel.filteredCities.first()
        assertEquals(2, filteredCities.size)
        assertTrue(filteredCities.any { it.name == "Berlin" })
        assertTrue(filteredCities.any { it.name == "Denver" })
    }*/
}
