package nox.uala.challenge.features.home.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import nox.uala.challenge.features.home.domain.model.City

@Parcelize
data class CityDto(
    @SerializedName("_id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("coord") val coordinates: CoordinatesDto?
) : Parcelable {

    @Parcelize
    data class CoordinatesDto(
        @SerializedName("lat") val lat: Double?,
        @SerializedName("lon") val lon: Double?
    ) : Parcelable
}

fun CityDto.toDomain(isFavorite: Boolean = false): City? {
    if (id == null || name.isNullOrEmpty() || country.isNullOrEmpty()) {
        return null
    }

    return City(
        id = id,
        name = name,
        country = country,
        lat = coordinates?.lat ?: 0.0,
        lon = coordinates?.lon ?: 0.0,
        isFavorite = isFavorite
    )
}