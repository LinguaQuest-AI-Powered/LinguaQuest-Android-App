package com.iti.linguaquest.features.profile.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import com.iti.linguaquest.features.profile.domain.model.PasswordUpdateStatus
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(oldPass: String, newPass: String): LinguaQuestResult<PasswordUpdateStatus, LinguaQuestDataError> =
        repository.changePassword(oldPass, newPass)

}