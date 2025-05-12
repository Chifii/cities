package nox.uala.challenge.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nox.uala.challenge.features.home.data.local.CityDatabase
import nox.uala.challenge.features.home.data.local.dao.CityDao
import nox.uala.challenge.features.home.data.repository.CitySearchCache
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCityDatabase(@ApplicationContext context: Context): CityDatabase {
        return Room.databaseBuilder(
            context,
            CityDatabase::class.java,
            "cities_database"
        )
            .addMigrations(CityDatabase.Companion.MIGRATION_1_2)
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideCityDao(database: CityDatabase): CityDao {
        return database.cityDao()
    }

    @Provides
    @Singleton
    fun provideCitySearchCache(): CitySearchCache {
        return CitySearchCache()
    }
}