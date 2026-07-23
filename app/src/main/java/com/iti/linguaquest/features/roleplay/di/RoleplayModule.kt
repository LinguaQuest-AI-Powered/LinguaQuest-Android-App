package com.iti.linguaquest.features.roleplay.di

import com.iti.linguaquest.features.roleplay.data.repository.RoleplayRepositoryImpl
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import com.google.firebase.Firebase
import com.google.firebase.ai.LiveGenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.PublicPreviewAPI
import dagger.Binds
import dagger.Module
import dagger.Provides
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

}
