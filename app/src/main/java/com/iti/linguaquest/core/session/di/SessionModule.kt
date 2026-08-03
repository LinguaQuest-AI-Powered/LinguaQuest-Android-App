package com.iti.linguaquest.core.session.di

import com.iti.linguaquest.core.session.SessionEventBus
import com.iti.linguaquest.core.session.SessionEventBusImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SessionModule {

    @Binds
    @Singleton
    abstract fun bindSessionEventBus(
        impl: SessionEventBusImpl
    ): SessionEventBus
}
