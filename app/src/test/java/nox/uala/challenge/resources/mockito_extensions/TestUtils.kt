package nox.uala.challenge.resources.mockito_extensions

import nox.uala.challenge.features.home.data.local.entity.CityEntity
import nox.uala.challenge.features.home.data.model.CityDto
import nox.uala.challenge.features.home.domain.model.City

val testCities = listOf(
    City(1, "Amsterdam", "NL", 52.3676, 4.9041, false),
    City(2, "Berlin", "DE", 52.5200, 13.4050, true),
    City(3, "Chicago", "US", 41.8781, -87.6298, false),
    City(4, "Denver", "US", 39.7392, -104.9903, true),
    City(5, "London", "UK", 51.5074, -0.1278, false),
    City(6, "New York", "US", 40.7128, -74.0060, false),
    City(7, "Paris", "FR", 48.8566, 2.3522, false),
    City(8, "Sydney", "AU", -33.8688, 151.2093, false),
    City(9, "Tokyo", "JP", 35.6762, 139.6503, true)
)

val testCity = City(
    id = 1,
    name = "Amsterdam",
    country = "NL",
    lat = 52.3676,
    lon = 4.9041,
    isFavorite = false
)

val testCityDtos = listOf(
    CityDto(
        id = 1,
        name = "New York",
        country = "US",
        coordinates = CityDto.CoordinatesDto(40.7128, -74.0060)
    ),
    CityDto(
        id = 2,
        name = "London",
        country = "UK",
        coordinates = CityDto.CoordinatesDto(51.5074, -0.1278)
    ),
    CityDto(
        id = 3,
        name = "Paris",
        country = "FR",
        coordinates = CityDto.CoordinatesDto(48.8566, 2.3522)
    ),
    CityDto(
        id = 4,
        name = "Tokyo",
        country = "JP",
        coordinates = CityDto.CoordinatesDto(35.6762, 139.6503)
    )
)

val testCityEntities = listOf(
    CityEntity(1, "New York", "US", 40.7128, -74.0060, false),
    CityEntity(2, "London", "UK", 51.5074, -0.1278, true),
    CityEntity(3, "Paris", "FR", 48.8566, 2.3522, false),
    CityEntity(4, "Tokyo", "JP", 35.6762, 139.6503, true)
)