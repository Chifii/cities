package nox.uala.challenge.features.home.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nox.uala.challenge.features.home.data.repository.HomeRepositoryImpl
import nox.uala.challenge.features.home.data.service.HomeService
import nox.uala.challenge.features.home.domain.repository.HomeRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    companion object {
        @Provides
        @Singleton
        fun provideApiService(retrofit: Retrofit): HomeService {
            return retrofit.create(HomeService::class.java)
        }
    }
}
