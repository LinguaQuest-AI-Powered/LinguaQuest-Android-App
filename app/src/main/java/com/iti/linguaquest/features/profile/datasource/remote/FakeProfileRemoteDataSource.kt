package com.iti.linguaquest.features.profile.datasource.remote

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.domain.model.AchievementPreview
import com.iti.linguaquest.features.profile.domain.model.AchievementStatus
import com.iti.linguaquest.features.profile.domain.model.AchievementsSummary
import com.iti.linguaquest.features.profile.domain.model.LanguageJourney
import com.iti.linguaquest.features.profile.domain.model.LeaderboardPreviewEntry
import com.iti.linguaquest.features.profile.domain.model.LeaderboardSummary
import com.iti.linguaquest.features.profile.domain.model.ProfileStats
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class FakeProfileRemoteDataSource @Inject constructor() : ProfileRepository {

    override suspend fun getProfileSummary(): LinguaQuestResult<ProfileSummary, LinguaQuestDataError> {
        delay(600.milliseconds)

        return LinguaQuestResult.Success(
            ProfileSummary(
                id = 1234,
                username = "mohamed_ali",
                name = "Mohamed Ali",
                photoUrl = "/media/avatars/alex.jpg",
                level = 12,
                stats = ProfileStats(coins = 1250, totalXp = 4500, streakDays = 7, worldsCount = 2),
                languageJourney = LanguageJourney(
                    languageId = 2,
                    name = "French",
                    code = "fr",
                    level = 12,
                    journeyLabel = "Intermediate Journey",
                    currentXp = 2460,
                    nextMilestoneXp = 3000
                ),
                achievementsSummary = AchievementsSummary(
                    earnedCount = 8,
                    totalCount = 23,
                    preview = listOf(
                        AchievementPreview(
                            id = 1,
                            name = "Wild Explorer",
                            description = "Complete 5 lessons in...",
                            iconUrl = "/media/achievements/wild-explorer.png",
                            status = AchievementStatus.EARNED,
                            progressPercent = 100
                        )
                    )
                ),
                leaderboardSummary = LeaderboardSummary(
                    myRank = 100,
                    preview = listOf(
                        LeaderboardPreviewEntry(
                            99,
                            501,
                            "Sacagawea",
                            "/media/avatars/501.jpg",
                            13,
                            2750,
                            false
                        ),
                        LeaderboardPreviewEntry(
                            100,
                            1234,
                            "Mohamed Ali",
                            "/media/avatars/alex.jpg",
                            12,
                            3150,
                            true
                        ),
                        LeaderboardPreviewEntry(
                            101,
                            502,
                            "Zheng He",
                            "/media/avatars/502.jpg",
                            11,
                            2600,
                            false
                        )
                    )
                )
            )
        )
    }
}