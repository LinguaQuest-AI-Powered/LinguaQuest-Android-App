package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.utils.TranscriptSanitizer
import com.iti.linguaquest.features.roleplay.domain.model.BossEvaluationResult
import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.ChatMessage
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class EvaluateBossStageUseCase @Inject constructor(
    private val repository: RoleplayRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(
        messages: List<ChatMessage>,
        scenario: BossScenario
    ): Result<BossEvaluationResult> {
        val userHasSpoken = messages.any { it.isUser }
        if (!userHasSpoken) {
            return Result.success(
                BossEvaluationResult(
                    task_completed = false,
                    fluency_score = 0,
                    feedback_message = "ERROR_NO_SPEECH"
                )
            )
        }

        val targetLanguage = userPreferencesRepository.targetLanguageName.firstOrNull() ?: "English"
        val userMessages = messages.filter { it.isUser }.map { it.text }
        val programmaticTargetPercentage = TranscriptSanitizer.calculateTargetLanguagePercentage(userMessages, targetLanguage)

        val transcript = messages.map { msg ->
            val cleanText = TranscriptSanitizer.sanitize(msg.text, targetLanguage)
            if (msg.isUser) "User: $cleanText" else "AI: $cleanText"
        }

        return repository.evaluateBossStage(transcript, scenario).map { assessmentResult ->
            val combinedTargetPercentage = minOf(assessmentResult.target_language_percentage, programmaticTargetPercentage)
            val finalTaskCompleted = assessmentResult.task_completed && combinedTargetPercentage >= MINIMUM_TARGET_LANGUAGE_PERCENTAGE
            val finalScore = if (!finalTaskCompleted) {
                assessmentResult.fluency_score.coerceAtMost(if (combinedTargetPercentage < MINIMUM_TARGET_LANGUAGE_PERCENTAGE) 35 else 50)
            } else {
                assessmentResult.fluency_score
            }
            val stars = when {
                !finalTaskCompleted -> 0
                finalScore >= 85 -> 3
                finalScore >= 70 -> 2
                finalScore >= 50 -> 1
                else -> 0
            }
            val (xpEarned, coinsEarned) = when (stars) {
                3 -> Pair(200, 75)
                2 -> Pair(150, 50)
                1 -> Pair(100, 25)
                else -> Pair(0, 0)
            }
            val filteredImprovements = TranscriptSanitizer.filterImprovements(assessmentResult.improvements)
            assessmentResult.copy(
                target_language_percentage = combinedTargetPercentage,
                task_completed = finalTaskCompleted,
                fluency_score = finalScore,
                improvements = filteredImprovements,
                stars = stars,
                xp_earned = xpEarned,
                coins_earned = coinsEarned
            )
        }
    }

    companion object {
        private const val MINIMUM_TARGET_LANGUAGE_PERCENTAGE = 50
    }
}
