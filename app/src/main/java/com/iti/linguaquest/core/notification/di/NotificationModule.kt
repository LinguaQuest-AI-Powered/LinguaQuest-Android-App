package com.iti.linguaquest.core.notification.di

import com.iti.linguaquest.core.notification.data.datasource.remote.NotificationApiService
import com.iti.linguaquest.core.notification.data.helper.FcmTokenManager
import com.iti.linguaquest.core.notification.data.repository.NotificationRepositoryImpl
import com.iti.linguaquest.core.notification.domain.provider.FcmTokenProvider
import com.iti.linguaquest.core.notification.domain.repository.NotificationRepository
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
}
