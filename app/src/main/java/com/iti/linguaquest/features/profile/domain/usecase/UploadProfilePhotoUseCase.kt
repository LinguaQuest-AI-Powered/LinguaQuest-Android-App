package com.iti.linguaquest.features.profile.editprofile.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import com.iti.linguaquest.features.profile.domain.model.ProfilePhoto
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadProfilePhotoUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(photo: MultipartBody.Part): LinguaQuestResult<ProfilePhoto, LinguaQuestDataError> =
        repository.uploadProfilePhoto(photo)
}
