package com.iti.linguaquest.features.roleplay.domain.model

sealed interface RoleplayLiveEvent {
    data class Transcription(val text: String) : RoleplayLiveEvent
    
    data class AudioChunk(val bytes: ByteArray) : RoleplayLiveEvent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as AudioChunk
            return bytes.contentEquals(other.bytes)
        }
        override fun hashCode(): Int = bytes.contentHashCode()
    }
    
    data class Error(val message: String) : RoleplayLiveEvent
    data object TurnComplete : RoleplayLiveEvent
}
