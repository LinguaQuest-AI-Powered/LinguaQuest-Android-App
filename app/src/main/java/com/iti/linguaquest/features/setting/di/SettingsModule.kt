package com.iti.linguaquest.features.setting.di

import com.iti.linguaquest.features.setting.system.AlarmScheduler
import com.iti.linguaquest.features.setting.system.AlarmSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    @Singleton
    abstract fun bindAlarmScheduler(
        impl: AlarmSchedulerImpl
    ): AlarmScheduler
}
