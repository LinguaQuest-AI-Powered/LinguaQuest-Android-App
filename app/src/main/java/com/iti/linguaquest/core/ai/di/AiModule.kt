package com.iti.linguaquest.core.ai.di

import com.iti.linguaquest.core.ai.client.AiClient
import com.iti.linguaquest.core.ai.client.FirebaseAiClient
import com.iti.linguaquest.core.ai.client.GeminiAiClient
import com.iti.linguaquest.core.ai.client.ItiGatewayAiClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AiModule {

    @Binds
    @Singleton
    abstract fun bindAiClient(impl: ItiGatewayAiClient): AiClient
}
