package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.R
import com.iti.linguaquest.core.domain.model.MiniGameReward
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderContradictionResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameConfig
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameHistory
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGuessResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderRewardChallenge
import javax.inject.Inject

class ResolveMindReaderRewardUseCase @Inject constructor() {

    operator fun invoke(
        challenge: MindReaderRewardChallenge,
        contradictionResult: MindReaderContradictionResult,
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
        contradictionResult: MindReaderContradictionResult,
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
                rewardCoins = MiniGameReward.MIND_READER_VICTORY.coins,
                rewardXp = MiniGameReward.MIND_READER_VICTORY.xp
            )
        } else {
            val reasonText = if (challenge.selectedEntity.id != challenge.correctEntity.id) {
                UiText.StringResource(R.string.mind_reader_reason_quiz_wrong)
            } else {
                UiText.StringResource(R.string.mind_reader_reason_contradiction)
            }
            MindReaderResult.Busted(
                guess = guess,
                history = history,
                reason = reasonText
            )
        }
    }

    private fun resolveStump(
        challenge: MindReaderRewardChallenge.Stump,
        contradictionResult: MindReaderContradictionResult,
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
                rewardCoins = MiniGameReward.MIND_READER_STUMP.coins,
                rewardXp = MiniGameReward.MIND_READER_STUMP.xp
            )
        } else {
            MindReaderResult.Busted(
                guess = guess,
                history = history,
                reason = UiText.StringResource(R.string.mind_reader_reason_stump_contradiction)
            )
        }
    }
}
