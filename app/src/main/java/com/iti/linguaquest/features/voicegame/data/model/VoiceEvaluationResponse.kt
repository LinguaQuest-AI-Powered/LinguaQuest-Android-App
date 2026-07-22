package com.iti.linguaquest.features.voicegame.data.model

import com.google.gson.annotations.SerializedName

data class VoiceEvaluationResponse(
    val rating: Int,
    @SerializedName("correct_words") val correctWords: List<String>,
    @SerializedName("wrong_words") val wrongWords: List<String>,
    val advice: String
)
