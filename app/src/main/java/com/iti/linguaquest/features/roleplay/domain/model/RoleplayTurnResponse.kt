package com.iti.linguaquest.features.roleplay.domain.model

data class RoleplayTurnResponse(
    val aiText: String,
    val aiTranslation: String,
    val audioBytes: ByteArray,
    val isObjectiveComplete: Boolean,
    val turnNumber: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RoleplayTurnResponse) return false
        return aiText == other.aiText &&
                aiTranslation == other.aiTranslation &&
                audioBytes.contentEquals(other.audioBytes) &&
                isObjectiveComplete == other.isObjectiveComplete &&
                turnNumber == other.turnNumber
    }

    override fun hashCode(): Int {
        var result = aiText.hashCode()
        result = 31 * result + aiTranslation.hashCode()
        result = 31 * result + audioBytes.contentHashCode()
        result = 31 * result + isObjectiveComplete.hashCode()
        result = 31 * result + turnNumber
        return result
    }
}
