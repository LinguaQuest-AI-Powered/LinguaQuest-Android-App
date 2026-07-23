package com.iti.linguaquest.features.all_worlds.data.repository

import android.util.Log
import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.all_worlds.data.mapper.toDomain
import com.iti.linguaquest.features.all_worlds.data.remote.WorldsApiService
import com.iti.linguaquest.features.all_worlds.data.remote.dto.WorldsDataDto
import com.iti.linguaquest.features.all_worlds.domain.model.WorldDifficulty
import com.iti.linguaquest.features.all_worlds.domain.model.WorldsData
import com.iti.linguaquest.features.all_worlds.domain.repository.WorldsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class WorldsRepositoryImpl @Inject constructor(
    private val api: WorldsApiService
) : WorldsRepository {

    override suspend fun getWorlds(
        difficulty: WorldDifficulty?
    ): LinguaQuestResult<WorldsData, LinguaQuestDataError> {
        return if (difficulty == null) {
            coroutineScope {
                val easyDeferred = async { safeApiCall { api.getWorlds("EASY") } }
                val mediumDeferred = async { safeApiCall { api.getWorlds("MEDIUM") } }
                val hardDeferred = async { safeApiCall { api.getWorlds("HARD") } }

                val easyResult = easyDeferred.await()
                val mediumResult = mediumDeferred.await()
                val hardResult = hardDeferred.await()

                val results = listOf(easyResult, mediumResult, hardResult)
                val successResults = results.filterIsInstance<LinguaQuestResult.Success<SuccessResponseDto<WorldsDataDto>>>()

                if (successResults.isNotEmpty()) {
                    val allWorlds = successResults.flatMap { it.data.data.worlds?.map { dto -> dto.toDomain() }.orEmpty() }
                    val totalCount = successResults.sumOf { it.data.data.totalCount ?: 0 }
                    LinguaQuestResult.Success(WorldsData(totalCount = totalCount, worlds = allWorlds))
                } else {
                    val failure = results.filterIsInstance<LinguaQuestResult.Failure<LinguaQuestDataError>>().firstOrNull()
                        ?: LinguaQuestResult.Failure(LinguaQuestDataError.Remote.UNKNOWN)
                    Log.e("WorldsRepository", "Error fetching all worlds: ${failure.error}")
                    failure
                }
            }
        } else {
            val difficultyString = difficulty.name
            val result = safeApiCall { api.getWorlds(difficultyString) }
            when (result) {
                is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data.toDomain())
                is LinguaQuestResult.Failure -> {
                    Log.e("WorldsRepository", "Error fetching worlds API for $difficultyString: ${result.error}")
                    result
                }
            }
        }
    }
}
