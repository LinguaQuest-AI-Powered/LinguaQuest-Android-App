package com.iti.linguaquest.features.leaderboard.data.datasource.remotedatesource

import com.google.gson.annotations.SerializedName

data class LeaderboardDataDto(
    @SerializedName("myRank") val myRank: Int,
    @SerializedName("topThree") val topThree: List<LeaderboardEntryDto>,
    @SerializedName("entries") val entries: List<LeaderboardEntryDto>
)
data class LeaderboardEntryDto(
    @SerializedName("rank") val rank: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("username") val username: String,
    @SerializedName("photoUrl") val photoUrl: String?,
    @SerializedName("level") val level: Int,
    @SerializedName("xp") val xp: Int,
    @SerializedName("isCurrentUser") val isCurrentUser: Boolean,

    )