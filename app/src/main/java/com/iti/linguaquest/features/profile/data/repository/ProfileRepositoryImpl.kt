package com.iti.linguaquest.features.profile.data.repository


import android.net.Uri
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.datasource.remote.ProfileRemoteDataSource
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {

    override suspend fun getProfileSummary() =
        remoteDataSource.getProfileSummary()

    override suspend fun uploadAvatar(imageUri: Uri): LinguaQuestResult<String, LinguaQuestDataError> =
        remoteDataSource.uploadAvatar(imageUri)
}
