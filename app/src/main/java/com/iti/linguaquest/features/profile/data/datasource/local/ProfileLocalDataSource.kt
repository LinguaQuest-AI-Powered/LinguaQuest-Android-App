package com.iti.linguaquest.features.profile.data.datasource.local

import kotlinx.coroutines.flow.Flow
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary


interface ProfileLocalDataSource {
    val cachedProfile: Flow<ProfileSummary?>
    val cachedAvatarUrl: Flow<String?>
    suspend fun saveProfile(profile: ProfileSummary)
    suspend fun clearProfile()
}