package com.iti.linguaquest.features.roleplay.di

import com.iti.linguaquest.features.roleplay.data.remote.GeminiRoleplayRemoteDataSource
import com.iti.linguaquest.features.roleplay.data.remote.GeminiRoleplayService
import com.iti.linguaquest.features.roleplay.data.remote.LiveRoleplayRemoteDataSource
import com.iti.linguaquest.features.roleplay.data.remote.LiveRoleplayService
import com.iti.linguaquest.features.roleplay.data.repository.RoleplayRepositoryImpl
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
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
        impl: LiveRoleplayService
    ): LiveRoleplayRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindGeminiRoleplayRemoteDataSource(
        impl: GeminiRoleplayService
    ): GeminiRoleplayRemoteDataSource
}
