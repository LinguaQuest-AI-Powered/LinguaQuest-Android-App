package com.iti.linguaquest.core.di

import com.iti.linguaquest.core.data.fake.FakeWorldsRepository
import com.iti.linguaquest.features.all_worlds.domain.repository.WorldsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WorldsModule {

    @Binds
    @Singleton
    abstract fun bindWorldsRepository(impl: FakeWorldsRepository): WorldsRepository
}
