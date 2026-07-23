package com.iti.linguaquest.features.roleplay.data.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayResult
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayTurnResponse
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeRoleplayRepository @Inject constructor() : RoleplayRepository {

    private var turnCount = 0
    private val maxTurns = 4

    private val scriptedResponses = listOf(
        RoleplayTurnResponse(
            aiText = "Bonjour ! Bienvenue au marché. Qu'est-ce que vous désirez aujourd'hui ?",
            aiTranslation = "Hello! Welcome to the market. What would you like today?",
            audioBytes = ByteArray(0),
            isObjectiveComplete = false,
            turnNumber = 1
        ),
        RoleplayTurnResponse(
            aiText = "Très bien ! Nous avons des pommes fraîches. Combien en voulez-vous ?",
            aiTranslation = "Very good! We have fresh apples. How many would you like?",
            audioBytes = ByteArray(0),
            isObjectiveComplete = false,
            turnNumber = 2
        ),
        RoleplayTurnResponse(
            aiText = "D'accord, trois pommes. Ce sera cinq euros, s'il vous plaît.",
            aiTranslation = "Alright, three apples. That will be five euros, please.",
            audioBytes = ByteArray(0),
            isObjectiveComplete = false,
            turnNumber = 3
        ),
        RoleplayTurnResponse(
            aiText = "Merci beaucoup ! Bonne journée et à bientôt !",
            aiTranslation = "Thank you very much! Have a nice day and see you soon!",
            audioBytes = ByteArray(0),
            isObjectiveComplete = true,
            turnNumber = 4
        )
    )

    override suspend fun initializeRoleplay(
        setting: String,
        taskDescription: String
    ): LinguaQuestResult<RoleplayTurnResponse, LinguaQuestDataError> {
        delay(1500L)
        turnCount = 1
        return LinguaQuestResult.Success(scriptedResponses[0])
    }

    override suspend fun submitUserAudio(
        audioBytes: ByteArray
    ): LinguaQuestResult<RoleplayTurnResponse, LinguaQuestDataError> {
        delay(2000L)
        turnCount = (turnCount + 1).coerceAtMost(maxTurns)
        val response = scriptedResponses.getOrElse(turnCount - 1) {
            scriptedResponses.last()
        }
        return LinguaQuestResult.Success(response)
    }

    override suspend fun evaluateRoleplay(): LinguaQuestResult<RoleplayResult, LinguaQuestDataError> {
        delay(1000L)
        return LinguaQuestResult.Success(
            RoleplayResult(
                passed = true,
                coinsAwarded = 25,
                feedback = "Excellent work! You successfully completed your market purchase in French. Your vocabulary and sentence structure were great!",
                totalTurns = turnCount
            )
        )
    }
}
