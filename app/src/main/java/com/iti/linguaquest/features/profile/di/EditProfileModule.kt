package com.iti.linguaquest.features.profile.di

import com.iti.linguaquest.features.profile.data.datasource.remote.EditProfileApiService
import com.iti.linguaquest.features.profile.data.datasource.remote.EditProfileRemoteDataSource
import com.iti.linguaquest.features.profile.data.datasource.remote.EditProfileRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EditProfileModule {

    @Binds
    @Singleton
    abstract fun bindEditProfileRemoteDataSource(
        impl: EditProfileRemoteDataSourceImpl
    ): EditProfileRemoteDataSource




    companion object {
        @Provides
        @Singleton
        fun provideEditProfileApiService(retrofit: Retrofit): EditProfileApiService {
            return retrofit.create(EditProfileApiService::class.java)
        }
    }
}