package com.iti.linguaquest.features.leaderboard.data.mapper

import com.iti.linguaquest.features.leaderboard.data.datasource.remotedatesource.LeaderboardDataDto
import com.iti.linguaquest.features.leaderboard.data.datasource.remotedatesource.LeaderboardEntryDto
import com.iti.linguaquest.features.leaderboard.domain.model.Leaderboard
import com.iti.linguaquest.features.leaderboard.domain.model.LeaderboardEntry

fun LeaderboardEntryDto.toDomain(): LeaderboardEntry = LeaderboardEntry(
    rank = rank,
    userId = userId,
    username = username,
    photoUrl = photoUrl,
    level = level,
    xp = xp,
    isCurrentUser = isCurrentUser
)

fun LeaderboardDataDto.toDomain(): Leaderboard = Leaderboard(
    myRank = myRank,
    topThree = topThree.map { it.toDomain() },
    entries = entries.map { it.toDomain() }
)