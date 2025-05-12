package nox.uala.challenge.features.home.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import nox.uala.challenge.features.home.data.local.dao.CityDao
import nox.uala.challenge.features.home.data.local.entity.CityEntity
import nox.uala.challenge.features.home.data.local.entity.CityFts

@Database(
    entities = [
        CityEntity::class,
        CityFts::class
    ],
    version = 2,
    exportSchema = false
)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE VIRTUAL TABLE IF NOT EXISTS `citiesFts` 
                    USING FTS4(
                        `id` INTEGER NOT NULL,
                        `name` TEXT NOT NULL,
                        `country` TEXT NOT NULL,
                        content=`cities`
                    )
                    """
                )

                database.execSQL(
                    """
                    INSERT INTO citiesFts(id, name, country)
                    SELECT id, name, country FROM cities
                    """
                )

                database.execSQL(
                    """
                    CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_cities_fts_BEFORE_UPDATE BEFORE UPDATE ON `cities` BEGIN
                        DELETE FROM `citiesFts` WHERE `id` = old.`id`;
                    END
                    """
                )
                database.execSQL(
                    """
                    CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_cities_fts_AFTER_UPDATE AFTER UPDATE ON `cities` BEGIN
                        INSERT INTO `citiesFts`(`id`, `name`, `country`) VALUES (new.`id`, new.`name`, new.`country`);
                    END
                    """
                )
                database.execSQL(
                    """
                    CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_cities_fts_BEFORE_DELETE BEFORE DELETE ON `cities` BEGIN
                        DELETE FROM `citiesFts` WHERE `id` = old.`id`;
                    END
                    """
                )
                database.execSQL(
                    """
                    CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_cities_fts_AFTER_INSERT AFTER INSERT ON `cities` BEGIN
                        INSERT INTO `citiesFts`(`id`, `name`, `country`) VALUES (new.`id`, new.`name`, new.`country`);
                    END
                    """
                )
            }
        }
    }
}
