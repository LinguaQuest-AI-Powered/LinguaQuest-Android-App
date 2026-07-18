package com.iti.linguaquest.features.home.di



import com.iti.linguaquest.features.home.data.fake.FakeHomeRepository
import com.iti.linguaquest.features.home.data.remote.HomeApiService
import com.iti.linguaquest.features.home.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(impl: FakeHomeRepository): HomeRepository
    // Swap to HomeRepositoryImpl when backend is ready

    companion object {
        @Provides
        @Singleton
        fun provideHomeApiService(retrofit: Retrofit): HomeApiService =
            retrofit.create(HomeApiService::class.java)
    }
}