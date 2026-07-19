package com.iti.linguaquest.features.map.di

import com.iti.linguaquest.features.map.data.remote.MapApiService
import com.iti.linguaquest.features.map.data.repository.MapRepositoryImpl
import com.iti.linguaquest.features.map.domain.repository.MapRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MapModule {

    @Binds
    @Singleton
    abstract fun bindMapRepository(
        mapRepositoryImpl: MapRepositoryImpl
    ): MapRepository

    companion object {
        @Provides
        @Singleton
        fun provideMapApiService(retrofit: Retrofit): MapApiService =
            retrofit.create(MapApiService::class.java)
    }
}
