package com.iti.linguaquest.features.profile.data.datasource.remote

import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.data.mapper.toDomain
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary
import jakarta.inject.Inject

//class ProfileRemoteDataSourceImpl @Inject constructor(
//    private val api: ProfileApiService
//) : ProfileRemoteDataSource {
//
//    override suspend fun getProfileSummary():
//            LinguaQuestResult<ProfileSummary, LinguaQuestDataError> {
//
//        return when (val result = safeApiCall { api.getProfileSummary() }) {
//
//            is LinguaQuestResult.Success ->
//                LinguaQuestResult.Success(
//                    result.data.data.toDomain()
//                )
//
//            is LinguaQuestResult.Failure ->
//                result
//        }
//    }
//}

import android.content.Context
import android.net.Uri

import dagger.hilt.android.qualifiers.ApplicationContext


class ProfileRemoteDataSourceImpl @Inject constructor(
    private val api: ProfileApiService,
    @param:ApplicationContext private val context: Context
) : ProfileRemoteDataSource {

    override suspend fun getProfileSummary(): LinguaQuestResult<ProfileSummary, LinguaQuestDataError> {
        return when (val result = safeApiCall { api.getProfileSummary() }) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data.toDomain())
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun uploadAvatar(imageUri: Uri): LinguaQuestResult<String, LinguaQuestDataError> {
        return when (val result = safeApiCall {
            val part = imageUri.toMultipartBodyPart(context, partName = "photo")
            api.uploadAvatarPhoto(part)
        }) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data.photoUrl)
            is LinguaQuestResult.Failure -> result
        }
    }
}
