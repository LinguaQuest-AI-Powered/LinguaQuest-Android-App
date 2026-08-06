package com.iti.linguaquest.features.notification.di

import com.iti.linguaquest.features.notification.data.datasource.local.NotificationLocalDataSource
import com.iti.linguaquest.features.notification.data.datasource.local.NotificationLocalDataSourceImpl
import com.iti.linguaquest.features.notification.data.datasource.remote.NotificationApiService
import com.iti.linguaquest.features.notification.data.datasource.remote.NotificationRemoteDataSource
import com.iti.linguaquest.features.notification.data.datasource.remote.NotificationRemoteDataSourceImpl
import com.iti.linguaquest.features.notification.data.helper.FcmTokenManager
import com.iti.linguaquest.features.notification.data.repository.NotificationRepositoryImpl
import com.iti.linguaquest.features.notification.domain.provider.FcmTokenProvider
import com.iti.linguaquest.features.notification.domain.repository.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideNotificationApiService(retrofit: Retrofit): NotificationApiService {
        return retrofit.create(NotificationApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationRepositoryBindings {

    @Binds
    @Singleton
    abstract fun bindFcmTokenProvider(
        impl: FcmTokenManager
    ): FcmTokenProvider

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRemoteDataSource(
        impl: NotificationRemoteDataSourceImpl
    ): NotificationRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindNotificationLocalDataSource(
        impl: NotificationLocalDataSourceImpl
    ): NotificationLocalDataSource
}
