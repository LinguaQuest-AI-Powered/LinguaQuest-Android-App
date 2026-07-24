package com.iti.linguaquest.features.game.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VerifyLevelDto(
    @SerializedName("isMatch") val isMatch: Boolean? = false,
    @SerializedName("xpEarned") val xpEarned: Int? = 0,
    @SerializedName("coinsEarned") val coinsEarned: Int? = 0,
    @SerializedName("level") val level: Int? = 0,
    @SerializedName("levelProgressPercentage") val levelProgressPercentage: Int? = 0
)
