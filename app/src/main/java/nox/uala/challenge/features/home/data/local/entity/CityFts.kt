package nox.uala.challenge.features.home.data.local.entity

import androidx.room.Entity
import androidx.room.Fts4

/**
 * Entidad FTS4 (Full-Text Search) para búsquedas rápidas de texto.
 * Esta tabla virtual permite búsquedas de texto ultrarrápidas en SQLite.
 */
@Fts4(contentEntity = CityEntity::class)
@Entity(tableName = "citiesFts")
data class CityFts(
    val id: Int,
    val name: String,
    val country: String
)
