package com.iti.linguaquest.features.game.data.fake

import com.iti.linguaquest.core.result.AppError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.game.domain.model.Hint
import com.iti.linguaquest.features.game.domain.model.VerifyLevelResult
import com.iti.linguaquest.features.game.domain.repository.LevelRepository
import kotlinx.coroutines.delay
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class FakeLevelRepository @Inject constructor() : LevelRepository {
    override suspend fun startLevel(worldId: Int, order: Int): LinguaQuestResult<String, AppError> {
        delay(400.milliseconds)
        return LinguaQuestResult.Success("APPLE")
    }

    override suspend fun changeWord(worldId: Int, order: Int): LinguaQuestResult<String, AppError> {
        delay(400.milliseconds)
        return LinguaQuestResult.Success("ORANGE")
    }

    override suspend fun verifyLevel(
        worldId: Int,
        order: Int,
        imageFile: File
    ): LinguaQuestResult<VerifyLevelResult, AppError> {
        delay(800.milliseconds)
        return LinguaQuestResult.Success(
            VerifyLevelResult(
                isMatch = true,
                xpEarned = 10,
                coinsEarned = 15,
                level = 1,
                levelProgressPercentage = 50
            )
        )
    }

    override suspend fun getHint(worldId: Int, order: Int): LinguaQuestResult<Hint, AppError> {
        delay(400.milliseconds)
        return LinguaQuestResult.Success(
            Hint(
                hint = "شيء تأكله وله لون أحمر أو أخضر",
                coinsSpent = 25,
                remainingCoins = 1200
            )
        )
    }
}
