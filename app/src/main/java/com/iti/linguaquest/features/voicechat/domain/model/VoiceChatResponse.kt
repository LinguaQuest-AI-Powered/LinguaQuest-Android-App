package com.iti.linguaquest.features.voicechat.domain.model

data class VoiceChatResponse(
    val text: String? = null,
    val audioBytes: ByteArray? = null,
    val isEndOfTurn: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VoiceChatResponse

        if (text != other.text) return false
        if (audioBytes != null) {
            if (other.audioBytes == null) return false
            if (!audioBytes.contentEquals(other.audioBytes)) return false
        } else if (other.audioBytes != null) return false
        if (isEndOfTurn != other.isEndOfTurn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = text?.hashCode() ?: 0
        result = 31 * result + (audioBytes?.contentHashCode() ?: 0)
        result = 31 * result + isEndOfTurn.hashCode()
        return result
    }
}
