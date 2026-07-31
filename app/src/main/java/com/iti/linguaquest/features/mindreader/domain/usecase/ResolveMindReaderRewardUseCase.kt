package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameConfig
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameHistory
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGuessResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderRewardChallenge
import javax.inject.Inject

class ResolveMindReaderRewardUseCase @Inject constructor() {

    operator fun invoke(
        challenge: MindReaderRewardChallenge,
        contradictionResult: com.iti.linguaquest.features.mindreader.domain.model.MindReaderContradictionResult,
        config: MindReaderGameConfig,
        history: MindReaderGameHistory
    ): MindReaderResult {
        return when (challenge) {
            is MindReaderRewardChallenge.PopQuiz -> resolvePopQuiz(
                challenge = challenge,
                contradictionResult = contradictionResult,
                config = config,
                history = history
            )

            is MindReaderRewardChallenge.Stump -> resolveStump(
                challenge = challenge,
                contradictionResult = contradictionResult,
                config = config,
                history = history
            )
        }
    }

    private fun resolvePopQuiz(
        challenge: MindReaderRewardChallenge.PopQuiz,
        contradictionResult: com.iti.linguaquest.features.mindreader.domain.model.MindReaderContradictionResult,
        config: MindReaderGameConfig,
        history: MindReaderGameHistory
    ): MindReaderResult {
        val guessedEntity = challenge.correctEntity
        val guess = MindReaderGuessResult(
            entity = guessedEntity,
            confidence = 1.0
        )

        return if (challenge.selectedEntity.id == challenge.correctEntity.id && contradictionResult.isHonest) {
            MindReaderResult.Victory(
                guess = guess,
                history = history,
                rewardCoins = config.correctRewardCoins,
                rewardXp = config.correctRewardXp
            )
        } else {
            MindReaderResult.Busted(
                guess = guess,
                history = history,
                reason = if (challenge.selectedEntity.id != challenge.correctEntity.id) {
                    "Pop quiz answer was incorrect."
                } else {
                    "Answer matched, but gameplay history contradicted the target word."
                }
            )
        }
    }

    private fun resolveStump(
        challenge: MindReaderRewardChallenge.Stump,
        contradictionResult: com.iti.linguaquest.features.mindreader.domain.model.MindReaderContradictionResult,
        config: MindReaderGameConfig,
        history: MindReaderGameHistory
    ): MindReaderResult {
        val guess = MindReaderGuessResult(
            entity = challenge.selectedEntity,
            confidence = 1.0
        )

        return if (contradictionResult.isHonest) {
            MindReaderResult.Victory(
                guess = guess,
                history = history,
                rewardCoins = config.stumpBonusCoins,
                rewardXp = config.stumpBonusXp
            )
        } else {
            MindReaderResult.Busted(
                guess = guess,
                history = history,
                reason = "The selected word contradicts the answers given during the game."
            )
        }
    }
}
