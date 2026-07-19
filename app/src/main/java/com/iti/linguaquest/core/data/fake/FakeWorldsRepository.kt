package com.iti.linguaquest.core.data.fake

import com.iti.linguaquest.features.all_worlds.domain.model.World
import com.iti.linguaquest.features.all_worlds.domain.model.WorldDifficulty
import com.iti.linguaquest.features.all_worlds.domain.model.WorldStatus
import com.iti.linguaquest.features.all_worlds.domain.model.WorldsData
import com.iti.linguaquest.features.all_worlds.domain.repository.WorldsRepository
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeWorldsRepository @Inject constructor() : WorldsRepository {

    override suspend fun getWorlds(
        languageId: Int?,
        difficulty: WorldDifficulty?
    ): LinguaQuestResult<WorldsData, LinguaQuestDataError> {
        delay(600)
        
        val allWorlds = listOf(
            World(10, "Kitchen World", "/media/worlds/kitchen.jpg", WorldDifficulty.EASY, WorldStatus.IN_PROGRESS, 40, 20, 8),
            World(11, "City World", "/media/worlds/city.jpg", WorldDifficulty.MEDIUM, WorldStatus.IN_PROGRESS, 10, 20, 2),
            World(12, "Park World", "/media/worlds/park.jpg", WorldDifficulty.EASY, WorldStatus.LOCKED, 0, 20, 0),
            World(13, "School World", "/media/worlds/school.jpg", WorldDifficulty.HARD, WorldStatus.LOCKED, 0, 20, 0),
            World(14, "Office World", "/media/worlds/office.jpg", WorldDifficulty.MEDIUM, WorldStatus.LOCKED, 0, 20, 0),
            World(15, "Gym World", "/media/worlds/gym.jpg", WorldDifficulty.HARD, WorldStatus.LOCKED, 0, 20, 0)
        )
        
        val filteredWorlds = if (difficulty != null) {
            allWorlds.filter { it.difficulty == difficulty }
        } else {
            allWorlds
        }

        return LinguaQuestResult.Success(
            WorldsData(
                totalCount = filteredWorlds.size,
                worlds = filteredWorlds
            )
        )
    }
}
