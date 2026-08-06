package com.iti.linguaquest.features.voicegame.presentation.model


data class VoiceResultUi(
    val rating: Int,
    val correctWords: List<String>,
    val wrongWords: List<String>,
    val advice: String,
    val coinsAwarded: Int,
    val xpAwarded: Int,
    val isPassed: Boolean,
    val lessonId: Int,
    val sentence: String,
    val coinsBeforeAward: Int,
    val xpBeforeAward: Int
)