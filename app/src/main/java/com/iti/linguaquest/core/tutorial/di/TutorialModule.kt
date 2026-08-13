package com.iti.linguaquest.core.tutorial.di

import com.iti.linguaquest.core.tutorial.data.TutorialRepositoryImpl
import com.iti.linguaquest.core.tutorial.domain.TourRegistry
import com.iti.linguaquest.core.tutorial.domain.TutorialManager
import com.iti.linguaquest.core.tutorial.domain.repository.TutorialRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TutorialModule {

    @Binds
    @Singleton
    abstract fun bindTutorialRepository(
        impl: TutorialRepositoryImpl
    ): TutorialRepository

    companion object {
        @Provides
        @Singleton
        fun provideTutorialManager(
            repository: TutorialRepository,
            tourRegistry: TourRegistry,
            scope: CoroutineScope
        ): TutorialManager {
            return TutorialManager(repository, tourRegistry, scope)
        }
    }
}
