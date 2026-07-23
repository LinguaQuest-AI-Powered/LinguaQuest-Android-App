package com.iti.linguaquest.features.voicechat.di

import com.google.firebase.Firebase
import com.google.firebase.ai.LiveGenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.AudioTranscriptionConfig
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.PublicPreviewAPI
import com.google.firebase.ai.type.ResponseModality
import com.google.firebase.ai.type.liveGenerationConfig
import com.iti.linguaquest.features.voicechat.data.datasource.VoiceChatRemoteDataSource
import com.iti.linguaquest.features.voicechat.data.datasource.VoiceChatRemoteDataSourceImpl
import com.iti.linguaquest.features.voicechat.data.repository.VoiceChatRepositoryImpl
import com.iti.linguaquest.features.voicechat.domain.repository.VoiceChatRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VoiceChatModule {

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(
        impl: VoiceChatRemoteDataSourceImpl
    ): VoiceChatRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindRepository(
        impl: VoiceChatRepositoryImpl
    ): VoiceChatRepository

    companion object {
        @Provides
        @OptIn(PublicPreviewAPI::class)
        fun provideLiveModel(): LiveGenerativeModel =
            Firebase.ai(backend = GenerativeBackend.googleAI()).liveModel(
                modelName = "gemini-3.5-flash-lite",
                generationConfig = liveGenerationConfig {
                    responseModality = ResponseModality.AUDIO
                    inputAudioTranscription = AudioTranscriptionConfig()
                    outputAudioTranscription = AudioTranscriptionConfig()
                }
            )
    }
}
