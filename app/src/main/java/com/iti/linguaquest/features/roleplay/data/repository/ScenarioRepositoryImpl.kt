package com.iti.linguaquest.features.roleplay.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iti.linguaquest.features.roleplay.data.datasource.local.ScenarioLocalDataSource
import com.iti.linguaquest.features.roleplay.data.dto.ScenarioDto
import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.repository.ScenarioRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScenarioRepositoryImpl @Inject constructor(
    private val localDataSource: ScenarioLocalDataSource
) : ScenarioRepository {

    private val gson = Gson()

    override suspend fun getBossScenarios(languageCode: String): List<BossScenario> {
        val jsonString = localDataSource.getScenariosJson(languageCode)
        val listType = object : TypeToken<List<ScenarioDto>>() {}.type
        val dtoList: List<ScenarioDto> = gson.fromJson(jsonString, listType)
        
        return dtoList.map { dto ->
            BossScenario(
                id = dto.id,
                worldId = dto.worldId,
                bossName = dto.bossName,
                roleDescription = dto.roleDescription,
                objective = dto.objective,
                voiceName = dto.voiceName
            )
        }
    }
}
