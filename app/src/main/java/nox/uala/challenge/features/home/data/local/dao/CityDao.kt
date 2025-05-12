package nox.uala.challenge.features.home.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import nox.uala.challenge.features.home.data.local.entity.CityEntity

@Dao
interface CityDao {
    @Query("SELECT * FROM cities ORDER BY name, country")
    suspend fun getAllCities(): List<CityEntity>

    @Query("SELECT * FROM cities WHERE isFavorite = 1")
    suspend fun getFavoriteCities(): List<CityEntity>

    /**
     * Búsqueda por prefijo optimizada con FTS.
     * Este método implementa la búsqueda exacta por prefijo donde:
     * - "A" encontrará Alabama, Albuquerque, Anaheim, Arizona
     * - "s" encontrará Sydney (insensible a mayúsculas/minúsculas)
     * - "Al" encontrará Alabama, Albuquerque
     * - "Alb" encontrará solo Albuquerque
     */
    @Transaction
    @Query("SELECT * FROM cities WHERE name LIKE :prefix || '%' AND isFavorite = :favoritesOnly ORDER BY name LIMIT 50")
    suspend fun getCitiesByPrefix(prefix: String, favoritesOnly: Boolean = false): List<CityEntity>

    /**
     * Búsqueda de favoritos por prefijo optimizada con FTS
     */
    @Transaction
    @Query(
        """
        SELECT c.* FROM cities c
        JOIN citiesFts fts ON c.id = fts.id
        WHERE fts.name LIKE :prefix || '%' AND c.isFavorite = 1
        ORDER BY c.name, c.country
    """
    )
    suspend fun getFavoriteCitiesByPrefix(prefix: String): List<CityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityEntity>)

    @Query("UPDATE cities SET isFavorite = :isFavorite WHERE id = :cityId")
    suspend fun updateFavoriteStatus(cityId: Int, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM cities")
    suspend fun getCityCount(): Int

    @Query("SELECT id FROM cities WHERE isFavorite = 1")
    suspend fun getFavoriteIds(): List<Int>
}
