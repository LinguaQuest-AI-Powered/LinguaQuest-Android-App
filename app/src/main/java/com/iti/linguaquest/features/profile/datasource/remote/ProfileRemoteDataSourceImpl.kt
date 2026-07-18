package com.iti.linguaquest.features.profile.datasource.remote

import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.data.mapper.toDomain
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary
import jakarta.inject.Inject

class ProfileRemoteDataSourceImpl @Inject constructor(
    private val api: ProfileApiService
) : ProfileRemoteDataSource {

    override suspend fun getProfileSummary():
            LinguaQuestResult<ProfileSummary, LinguaQuestDataError> {

        return when (val result = safeApiCall { api.getProfileSummary() }) {

            is LinguaQuestResult.Success ->
                LinguaQuestResult.Success(
                    result.data.data.toDomain()
                )

            is LinguaQuestResult.Failure ->
                result
        }
    }
}
