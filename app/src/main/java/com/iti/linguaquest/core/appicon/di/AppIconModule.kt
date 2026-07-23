package com.iti.linguaquest.core.appicon.di

import com.iti.linguaquest.core.appicon.data.AppIconStateRepositoryImpl
import com.iti.linguaquest.core.appicon.dataimport.AndroidAppIconController
import com.iti.linguaquest.core.appicon.domain.AppIconController
import com.iti.linguaquest.core.appicon.domain.AppIconRule
import com.iti.linguaquest.core.appicon.domain.AppIconStateRepository
import com.iti.linguaquest.core.appicon.domain.SeasonalIconWindow
import com.iti.linguaquest.core.appicon.rules.AngryAppIconRule
import com.iti.linguaquest.core.appicon.rules.DefaultAppIconRule
import com.iti.linguaquest.core.appicon.rules.FireAppIconRule
import com.iti.linguaquest.core.appicon.rules.RewardAppIconRule
import com.iti.linguaquest.core.appicon.rules.SeasonalAppIconRule
import com.iti.linguaquest.core.appicon.rules.SleepAppIconRule
import com.iti.linguaquest.core.appicon.util.AppIconClock
import com.iti.linguaquest.core.appicon.util.SystemAppIconClock
import com.iti.linguaquest.core.appicon.worker.AppIconWorkScheduler
import com.iti.linguaquest.core.appicon.worker.AppIconWorkSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppIconDataStoreModule {
    @Provides
    fun provideSeasonalWindows(): List<SeasonalIconWindow> = emptyList()

    @Provides
    fun provideAppIconRules(
        rewardRule: RewardAppIconRule,
        fireRule: FireAppIconRule,
        sleepRule: SleepAppIconRule,
        angryRule: AngryAppIconRule,
        seasonalRule: SeasonalAppIconRule,
        defaultRule: DefaultAppIconRule
    ): List<AppIconRule> = listOf(
        rewardRule,
        fireRule,
        sleepRule,
        angryRule,
        seasonalRule,
        defaultRule
    )
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppIconBindingsModule {
    @Binds
    @Singleton
    abstract fun bindAppIconClock(impl: SystemAppIconClock): AppIconClock

    @Binds
    @Singleton
    abstract fun bindAppIconStateRepository(
        impl: AppIconStateRepositoryImpl
    ): AppIconStateRepository

    @Binds
    @Singleton
    abstract fun bindAppIconWorkScheduler(
        impl: AppIconWorkSchedulerImpl
    ): AppIconWorkScheduler

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class AppIconModule {

        @Binds
        @Singleton
        abstract fun bindAppIconController(
            impl: AndroidAppIconController
        ): AppIconController
    }
}
