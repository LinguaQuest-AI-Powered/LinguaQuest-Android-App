package com.iti.linguaquest.features.home.di



import com.iti.linguaquest.features.home.data.fake.FakeHomeRepository
import com.iti.linguaquest.features.home.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(impl: FakeHomeRepository): HomeRepository
    // Swap to: abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository when ready
}