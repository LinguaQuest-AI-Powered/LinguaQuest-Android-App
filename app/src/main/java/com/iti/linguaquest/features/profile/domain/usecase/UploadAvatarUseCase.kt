package com.iti.linguaquest.features.profile.domain.usecase

import android.net.Uri
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UploadAvatarUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(imageUri: Uri): LinguaQuestResult<String, LinguaQuestDataError> =
        profileRepository.uploadAvatar(imageUri)
}