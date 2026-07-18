package com.iti.linguaquest.features.profile.di


import com.iti.linguaquest.features.profile.datasource.remote.FakeProfileRemoteDataSource
import com.iti.linguaquest.features.profile.datasource.remote.ProfileApiService
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {

    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: FakeProfileRemoteDataSource): ProfileRepository
    // Swap to ProfileRepositoryImpl when backend is ready

    companion object {
        @Provides
        @Singleton
        fun provideProfileApiService(retrofit: Retrofit): ProfileApiService =
            retrofit.create(ProfileApiService::class.java)
    }
}