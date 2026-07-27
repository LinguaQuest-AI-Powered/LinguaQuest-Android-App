package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.wallet.domain.usecase.AdjustWalletUseCase
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ClaimLockScreenMilestoneRewardUseCase @Inject constructor(
    private val repository: LockScreenRepository,
    private val adjustWalletUseCase: AdjustWalletUseCase,
    private val clearLockScreenWordsUseCase: ClearLockScreenWordsUseCase
) {
    suspend operator fun invoke(
        wordCount: Int,
        coinsReward: Int = 100
    ): LinguaQuestResult<Unit, LinguaQuestDataError> {
        if (wordCount <= 0 || wordCount % 10 != 0) {
            return LinguaQuestResult.Success(Unit)
        }

        val lastRewardedCount = repository.lastRewardedMilestoneCount.first()
        if (lastRewardedCount == wordCount) {
            return LinguaQuestResult.Success(Unit)
        }

        return when (val walletResult = adjustWalletUseCase(xpDelta = 0, coinsDelta = coinsReward)) {
            is LinguaQuestResult.Success -> {
                try {
                    repository.saveLastRewardedMilestoneCount(wordCount)
                    when (val clearResult = clearLockScreenWordsUseCase()) {
                        is LinguaQuestResult.Success -> LinguaQuestResult.Success(Unit)
                        is LinguaQuestResult.Failure -> clearResult
                    }
                } catch (_: Exception) {
                    LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
                }
            }

            is LinguaQuestResult.Failure -> walletResult
        }
    }
}
