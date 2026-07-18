package com.iti.linguaquest.features.profile.domain.usecase


import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileSummaryUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): LinguaQuestResult<ProfileSummary, LinguaQuestDataError> =
        profileRepository.getProfileSummary()
}