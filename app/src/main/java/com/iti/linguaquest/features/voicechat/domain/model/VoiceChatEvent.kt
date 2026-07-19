package com.iti.linguaquest.features.voicechat.domain.model

sealed interface VoiceChatEvent {
    data class TextChunk(val text: String) : VoiceChatEvent
    data class AudioChunk(val pcmData: ByteArray) : VoiceChatEvent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as AudioChunk

            return pcmData.contentEquals(other.pcmData)
        }

        override fun hashCode(): Int {
            return pcmData.contentHashCode()
        }
    }

    data object TurnComplete : VoiceChatEvent
    data class Error(val message: String) : VoiceChatEvent
}