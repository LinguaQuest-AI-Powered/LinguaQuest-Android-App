package com.iti.linguaquest.features.profile.data.repository


import android.net.Uri
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.data.datasource.local.ProfileLocalDataSource
import com.iti.linguaquest.features.profile.data.datasource.remote.ProfileRemoteDataSource
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProfileRemoteDataSource,
    private val localDataSource: ProfileLocalDataSource
) : ProfileRepository {
    override val cachedAvatarUrl: Flow<String?> = localDataSource.cachedAvatarUrl

    override suspend fun getProfileSummary(): LinguaQuestResult<ProfileSummary, LinguaQuestDataError> {
        val result = remoteDataSource.getProfileSummary()
        if (result is LinguaQuestResult.Success && result.data.photoUrl.isNotBlank()) {
            localDataSource.saveAvatarUrl(result.data.photoUrl)
        }
        return result
    }

    override suspend fun uploadAvatar(imageUri: Uri): LinguaQuestResult<String, LinguaQuestDataError> {
        val result = remoteDataSource.uploadAvatar(imageUri)
        if (result is LinguaQuestResult.Success) {
            localDataSource.saveAvatarUrl(result.data)
        }
        return result
    }
}
