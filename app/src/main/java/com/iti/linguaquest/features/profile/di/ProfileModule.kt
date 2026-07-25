package com.iti.linguaquest.features.profile.di


import android.content.Context
import coil.ImageLoader
import com.iti.linguaquest.features.profile.data.datasource.local.ProfileLocalDataSource
import com.iti.linguaquest.features.profile.data.datasource.local.ProfileLocalDataSourceImpl
import com.iti.linguaquest.features.profile.data.repository.ProfileRepositoryImpl
import com.iti.linguaquest.features.profile.data.datasource.remote.ProfileApiService
import com.iti.linguaquest.features.profile.data.datasource.remote.ProfileRemoteDataSource
import com.iti.linguaquest.features.profile.data.datasource.remote.ProfileRemoteDataSourceImpl
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {

    @Binds
    @Singleton
    abstract fun bindProfileRemoteDataSource(
        impl: ProfileRemoteDataSourceImpl
    ): ProfileRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindProfileLocalDataSource(
        impl: ProfileLocalDataSourceImpl
    ): ProfileLocalDataSource

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository

    companion object {
        @Provides
        @Singleton
        fun provideProfileApiService(retrofit: Retrofit): ProfileApiService =
            retrofit.create(ProfileApiService::class.java)

        @Provides
        @Singleton
        fun provideImageLoader(@ApplicationContext context: Context): ImageLoader =
            ImageLoader.Builder(context).build()
    }


}