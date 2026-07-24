package com.iti.linguaquest.features.profile.data.datasource.remote

import android.net.Uri
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary

interface ProfileRemoteDataSource {
    suspend fun getProfileSummary():
            LinguaQuestResult<ProfileSummary, LinguaQuestDataError>

    suspend fun uploadAvatar(imageUri: Uri): LinguaQuestResult<String, LinguaQuestDataError>
}
