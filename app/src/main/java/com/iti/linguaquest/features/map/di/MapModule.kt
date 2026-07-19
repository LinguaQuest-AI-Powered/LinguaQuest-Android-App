package com.iti.linguaquest.features.map.di

import com.iti.linguaquest.features.map.data.repository.MockMapRepository
import com.iti.linguaquest.features.map.domain.repository.MapRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MapModule {

    @Binds
    @Singleton
    abstract fun bindMapRepository(
        mockMapRepository: MockMapRepository
    ): MapRepository
}
