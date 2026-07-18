package com.iti.linguaquest.features.profile.domain.repository


import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary

interface ProfileRepository {
    suspend fun getProfileSummary(): LinguaQuestResult<ProfileSummary, LinguaQuestDataError>
}