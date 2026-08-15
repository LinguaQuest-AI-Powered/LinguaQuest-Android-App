package com.iti.linguaquest.features.roleplay.di

import com.iti.linguaquest.core.ai.roleplay.DeepSeekWalkieTalkieService
import com.iti.linguaquest.core.ai.roleplay.GeminiLiveStreamingService
import com.iti.linguaquest.core.ai.roleplay.LiveRoleplayRemoteDataSource
import com.iti.linguaquest.features.roleplay.data.datasource.local.ScenarioLocalDataSource
import com.iti.linguaquest.features.roleplay.data.datasource.local.ScenarioLocalDataSourceImpl
import com.iti.linguaquest.features.roleplay.data.datasource.remote.GeminiRoleplayRemoteDataSource
import com.iti.linguaquest.features.roleplay.data.datasource.remote.GeminiRoleplayService
import com.iti.linguaquest.features.roleplay.data.repository.RoleplayRepositoryImpl
import com.iti.linguaquest.features.roleplay.data.repository.ScenarioRepositoryImpl
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import com.iti.linguaquest.features.roleplay.domain.repository.ScenarioRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RoleplayModule {

    @Binds
    @Singleton
    abstract fun bindRoleplayRepository(
        impl: RoleplayRepositoryImpl
    ): RoleplayRepository

    @Binds
    @Singleton
    abstract fun bindLiveRoleplayRemoteDataSource(
        impl: GeminiLiveStreamingService
    ): LiveRoleplayRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindGeminiRoleplayRemoteDataSource(
        impl: GeminiRoleplayService
    ): GeminiRoleplayRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindScenarioLocalDataSource(
        impl: ScenarioLocalDataSourceImpl
    ): ScenarioLocalDataSource

    @Binds
    @Singleton
    abstract fun bindScenarioRepository(
        impl: ScenarioRepositoryImpl
    ): ScenarioRepository
}
