package com.iti.linguaquest.features.voicegame.domain.model

data class VoiceEvaluation(
    val rating: Int,
    val correctWords: List<String>,
    val wrongWords: List<String>,
    val advice: String
)
