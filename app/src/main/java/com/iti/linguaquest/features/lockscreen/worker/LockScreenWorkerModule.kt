package com.iti.linguaquest.features.lockscreen.worker

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LockScreenWorkerModule {

    @Binds
    @Singleton
    abstract fun bindVocabularyWorkScheduler(
        impl: VocabularyWorkSchedulerImpl
    ): VocabularyWorkScheduler
}
