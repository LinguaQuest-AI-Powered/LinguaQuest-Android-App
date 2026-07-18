package com.iti.linguaquest.features.profile.di


import com.iti.linguaquest.features.profile.data.fake.FakeProfileRepository
import com.iti.linguaquest.features.profile.data.remote.ProfileApiService
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
    abstract fun bindProfileRepository(impl: FakeProfileRepository): ProfileRepository
    // Swap to ProfileRepositoryImpl when backend is ready

    companion object {
        @Provides
        @Singleton
        fun provideProfileApiService(retrofit: Retrofit): ProfileApiService =
            retrofit.create(ProfileApiService::class.java)
    }
}