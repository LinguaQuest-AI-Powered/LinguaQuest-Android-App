package com.iti.linguaquest.features.profile.data.repository


import android.net.Uri
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.data.datasource.local.ProfileLocalDataSource
import com.iti.linguaquest.features.profile.data.datasource.remote.ProfileRemoteDataSource
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProfileRemoteDataSource,
    private val localDataSource: ProfileLocalDataSource
) : ProfileRepository {

    override val cachedProfile: Flow<ProfileSummary?> = localDataSource.cachedProfile
    override val cachedAvatarUrl: Flow<String?> = localDataSource.cachedAvatarUrl

    override suspend fun refreshProfileSummary(): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return when (val result = remoteDataSource.getProfileSummary()) {
            is LinguaQuestResult.Success -> {
                localDataSource.saveProfile(result.data)
                LinguaQuestResult.Success(Unit)
            }
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun uploadAvatar(imageUri: Uri): LinguaQuestResult<String, LinguaQuestDataError> {
        val result = remoteDataSource.uploadAvatar(imageUri)
        if (result is LinguaQuestResult.Success) {
            localDataSource.cachedProfile.firstOrNull()?.let { current ->
                localDataSource.saveProfile(current.copy(photoUrl = result.data))
            }
        }
        return result
    }
}