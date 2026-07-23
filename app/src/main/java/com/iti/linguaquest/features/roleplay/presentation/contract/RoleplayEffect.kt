package com.iti.linguaquest.features.roleplay.presentation.contract

sealed interface RoleplayEffect {
    data object NavigateToHome : RoleplayEffect
    data class PlayAiAudio(val audioBytes: ByteArray) : RoleplayEffect {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is PlayAiAudio) return false
            return audioBytes.contentEquals(other.audioBytes)
        }

        override fun hashCode(): Int = audioBytes.contentHashCode()
    }
}
