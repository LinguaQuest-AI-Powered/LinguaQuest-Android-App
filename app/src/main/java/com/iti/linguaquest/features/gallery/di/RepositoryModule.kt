package com.iti.linguaquest.features.gallery.di

import com.iti.linguaquest.features.gallery.data.datasource.remote.GalleryApiService
import com.iti.linguaquest.features.gallery.data.datasource.remote.GalleryRemoteDataSourceImpl
import com.iti.linguaquest.features.gallery.data.datasource.remote.WordRemoteDataSource
import com.iti.linguaquest.features.gallery.data.datasource.WordLocalDataSource
import com.iti.linguaquest.features.gallery.data.datasource.WordLocalDataSourceImpl
import com.iti.linguaquest.features.gallery.data.repository.WordRepositoryImpl
import com.iti.linguaquest.features.gallery.domain.repository.WordRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWordLocalDataSource(
        wordLocalDataSourceImpl: WordLocalDataSourceImpl
    ): WordLocalDataSource

    @Binds
    @Singleton
    abstract fun bindWordRepository(
        wordRepositoryImpl: WordRepositoryImpl
    ): WordRepository

    @Binds
    @Singleton
    abstract fun bindWordRemoteDataSource(
        wordRemoteDataSourceImpl: GalleryRemoteDataSourceImpl
    ): WordRemoteDataSource

    companion object {
        @Provides
        @Singleton
        fun provideGalleryApiService(retrofit: Retrofit): GalleryApiService =
            retrofit.create(GalleryApiService::class.java)
    }
}
