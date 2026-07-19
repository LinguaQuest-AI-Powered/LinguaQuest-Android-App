package com.iti.linguaquest.features.leaderboard.domain.model


data class Leaderboard(
    val myRank: Int,
    val topThree: List<LeaderboardEntry>,
    val entries: List<LeaderboardEntry>
)

data class LeaderboardEntry(
    val rank: Int,
    val userId: Int,
    val username: String,
    val photoUrl: String?,
    val level: Int,
    val xp: Int,
    val isCurrentUser: Boolean
)

enum class LeaderboardScope {
    GLOBAL,
    LANGUAGE
}
