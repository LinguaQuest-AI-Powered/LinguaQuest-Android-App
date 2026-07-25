package com.iti.linguaquest.features.profile.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import com.iti.linguaquest.features.profile.domain.model.UserProfile
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(username: String): LinguaQuestResult<UserProfile, LinguaQuestDataError> =
    repository.updateProfile(username)

}