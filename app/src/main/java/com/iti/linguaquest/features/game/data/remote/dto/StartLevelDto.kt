package com.iti.linguaquest.features.game.data.remote.dto

import com.google.gson.annotations.SerializedName

data class StartLevelDto(
    @SerializedName("targetWord") val targetWord: String? = null
)
