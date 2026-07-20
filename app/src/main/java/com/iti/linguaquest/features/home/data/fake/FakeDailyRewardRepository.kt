package com.iti.linguaquest.features.home.data.fake


import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.model.DailyRewardClaimResult
import com.iti.linguaquest.features.home.domain.model.DailyRewardStatus
import com.iti.linguaquest.features.home.domain.repository.DailyRewardRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class FakeDailyRewardRepository @Inject constructor() : DailyRewardRepository {

    private var claimedToday = false

    override suspend fun getStatus(): LinguaQuestResult<DailyRewardStatus, LinguaQuestDataError> {
        delay(400.milliseconds)
        return LinguaQuestResult.Success(
            DailyRewardStatus(
                claimedToday = claimedToday,
                currentDay = 3,
                cycleLength = 5,
                rewardCoins = 50,
                rewardXp = null,
                streakDays = 7
            )
        )
    }

    override suspend fun claim(): LinguaQuestResult<DailyRewardClaimResult, LinguaQuestDataError> {
        delay(400.milliseconds)
        if (claimedToday) {
            return LinguaQuestResult.Failure(
                LinguaQuestDataError.CustomServerMessage("You've already claimed today's reward")
            )
        }
        claimedToday = true
        return LinguaQuestResult.Success(
            DailyRewardClaimResult(
                coinsAwarded = 50,
                xpAwarded = null,
                newCoinsBalance = 96,
                newXpBalance = 1250,
                newStreakDays = 8,
                nextDay = 4
            )
        )
    }
}