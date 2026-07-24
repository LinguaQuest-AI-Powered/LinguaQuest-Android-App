package com.iti.linguaquest.features.home.data.fake

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.model.ActiveLanguage
import com.iti.linguaquest.features.home.domain.model.ContinueLesson
import com.iti.linguaquest.features.all_worlds.domain.model.World
import com.iti.linguaquest.features.home.domain.model.HomeSummary
import com.iti.linguaquest.features.all_worlds.domain.model.WorldDifficulty
import com.iti.linguaquest.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class FakeHomeRepository @Inject constructor() : HomeRepository {

    override suspend fun getHomeSummary(): LinguaQuestResult<HomeSummary, LinguaQuestDataError> {
        delay(600.milliseconds)

        return LinguaQuestResult.Success(
            HomeSummary(
                xp = 1250,
                coins = 46,
                streakDays = 7,
                activeLanguage = ActiveLanguage(1, "Spanish", "es", 12, 65),
                continueLesson = ContinueLesson(
                    worldId = 10,
                    worldName = "Kitchen World",
                    levelId = 145,
                    word = "Apple",
                    translation = "La Pomme",
                    imageUrl = "/media/words/apple.jpg",
                    sentence = "The apple is red"
                ),
                exploreWorlds = listOf(
                    World(10, "Kitchen World", "/media/worlds/kitchen.jpg", WorldDifficulty.EASY, 40, 20, 8),
                    World(11, "City World", "/media/worlds/city.jpg", WorldDifficulty.MEDIUM, 10, 20, 2)
                )
            )
        )
    }
}