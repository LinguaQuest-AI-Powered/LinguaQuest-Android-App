package com.iti.linguaquest.features.profile.data.repository


import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.data.mapper.toDomain
import com.iti.linguaquest.features.profile.datasource.remote.ProfileApiService
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApiService
) : ProfileRepository {

    override suspend fun getProfileSummary(): LinguaQuestResult<ProfileSummary, LinguaQuestDataError> {
        val result = safeApiCall { api.getProfileSummary() }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data.toDomain())
            is LinguaQuestResult.Failure -> result
        }
    }
}