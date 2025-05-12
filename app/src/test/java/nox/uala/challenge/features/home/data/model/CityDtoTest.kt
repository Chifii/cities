package nox.uala.challenge.features.home.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CityDtoTest {

    @Test
    fun `toDomain returns valid City when all fields are present`() {
        val cityDto = CityDto(
            id = 1,
            name = "New York",
            country = "US",
            coordinates = CityDto.CoordinatesDto(
                lat = 40.7128,
                lon = -74.0060
            )
        )

        val city = cityDto.toDomain()

        assertEquals(1, city?.id)
        assertEquals("New York", city?.name)
        assertEquals("US", city?.country)
        assertEquals(40.7128, city?.lat ?: 0.0, 0.0001)
        assertEquals(-74.0060, city?.lon ?: 0.0, 0.0001)
        assertEquals(false, city?.isFavorite)
    }

    @Test
    fun `toDomain returns null when id is missing`() {
        val cityDto = CityDto(
            id = null,
            name = "New York",
            country = "US",
            coordinates = CityDto.CoordinatesDto(
                lat = 40.7128,
                lon = -74.0060
            )
        )

        val city = cityDto.toDomain()

        assertNull(city)
    }

    @Test
    fun `toDomain returns null when name is missing or empty`() {
        val cityDtoNullName = CityDto(
            id = 1,
            name = null,
            country = "US",
            coordinates = CityDto.CoordinatesDto(lat = 40.7128, lon = -74.0060)
        )

        val cityDtoEmptyName = CityDto(
            id = 1,
            name = "",
            country = "US",
            coordinates = CityDto.CoordinatesDto(lat = 40.7128, lon = -74.0060)
        )

        val cityNullName = cityDtoNullName.toDomain()
        val cityEmptyName = cityDtoEmptyName.toDomain()

        assertNull(cityNullName)
        assertNull(cityEmptyName)
    }

    @Test
    fun `toDomain returns null when country is missing or empty`() {
        val cityDtoNullCountry = CityDto(
            id = 1,
            name = "New York",
            country = null,
            coordinates = CityDto.CoordinatesDto(lat = 40.7128, lon = -74.0060)
        )

        val cityDtoEmptyCountry = CityDto(
            id = 1,
            name = "New York",
            country = "",
            coordinates = CityDto.CoordinatesDto(lat = 40.7128, lon = -74.0060)
        )

        val cityNullCountry = cityDtoNullCountry.toDomain()
        val cityEmptyCountry = cityDtoEmptyCountry.toDomain()

        assertNull(cityNullCountry)
        assertNull(cityEmptyCountry)
    }

    @Test
    fun `toDomain uses default coordinates when coordinates are null`() {
        val cityDto = CityDto(
            id = 1,
            name = "New York",
            country = "US",
            coordinates = null
        )

        val city = cityDto.toDomain()

        assertEquals(1, city?.id)
        assertEquals("New York", city?.name)
        assertEquals("US", city?.country)
        assertEquals(0.0, city?.lat ?: 0.0, 0.0001)
        assertEquals(0.0, city?.lon ?: 0.0, 0.0001)
    }

    @Test
    fun `toDomain handles null lat and lon values`() {
        val cityDto = CityDto(
            id = 1,
            name = "New York",
            country = "US",
            coordinates = CityDto.CoordinatesDto(lat = null, lon = null)
        )

        val city = cityDto.toDomain()

        assertEquals(1, city?.id)
        assertEquals("New York", city?.name)
        assertEquals("US", city?.country)
        assertEquals(0.0, city?.lat ?: 0.0, 0.0001)
        assertEquals(0.0, city?.lon ?: 0.0, 0.0001)
    }

    @Test
    fun `list toDomain correctly maps favorites`() {
        val cityDtoList = listOf(
            CityDto(id = 1, name = "New York", country = "US", coordinates = null),
            CityDto(id = 2, name = "London", country = "UK", coordinates = null),
            CityDto(id = 3, name = "Paris", country = "FR", coordinates = null)
        )
        val favorites = listOf(1, 3)

        val cities = cityDtoList.mapNotNull { it.toDomain() }.map {
            it.copy(isFavorite = it.id in favorites)
        }

        assertEquals(3, cities.size)
        assertTrue(cities.find { it.id == 1 }?.isFavorite == true)
        assertTrue(cities.find { it.id == 2 }?.isFavorite == false)
        assertTrue(cities.find { it.id == 3 }?.isFavorite == true)
    }

    @Test
    fun `list toDomain filters out invalid cities`() {
        val cityDtoList = listOf(
            CityDto(id = 1, name = "New York", country = "US", coordinates = null),
            CityDto(id = null, name = "Invalid", country = "XX", coordinates = null),
            CityDto(id = 2, name = "", country = "UK", coordinates = null),
            CityDto(id = 3, name = "Paris", country = "FR", coordinates = null)
        )

        val cities = cityDtoList.mapNotNull { it.toDomain() }

        assertEquals(2, cities.size)
        assertEquals(1, cities[0].id)
        assertEquals(3, cities[1].id)
    }

    @Test
    fun `toDomain implementation correctly handles favorite flag`() {
        val cityDto = CityDto(
            id = 1,
            name = "New York",
            country = "US",
            coordinates = null
        )

        val cityNotFavorite = cityDto.toDomain(isFavorite = false)
        val cityFavorite = cityDto.toDomain(isFavorite = true)

        assertEquals(false, cityNotFavorite?.isFavorite)
        assertEquals(true, cityFavorite?.isFavorite)
    }

    @Test
    fun `toDomain preserves special characters in city names`() {
        val cityDto = CityDto(
            id = 1,
            name = "São Paulo",
            country = "BR",
            coordinates = null
        )

        val city = cityDto.toDomain()

        assertEquals("São Paulo", city?.name)
    }
}
