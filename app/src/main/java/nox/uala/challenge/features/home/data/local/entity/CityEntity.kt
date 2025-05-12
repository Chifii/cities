package nox.uala.challenge.features.home.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import nox.uala.challenge.features.home.domain.model.City

@Entity(
    tableName = "cities",
    indices = [
        Index(value = ["name"], name = "idx_city_name"),
        Index(value = ["isFavorite"], name = "idx_city_favorite")
    ]
)
data class CityEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val isFavorite: Boolean = false
) {
    fun toDomainModel(): City {
        return City(
            id = id,
            name = name,
            country = country,
            lat = latitude,
            lon = longitude,
            isFavorite = isFavorite
        )
    }
}
