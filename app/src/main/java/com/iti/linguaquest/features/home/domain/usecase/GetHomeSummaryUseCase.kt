package com.iti.linguaquest.features.home.domain.usecase


import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.model.HomeSummary
import com.iti.linguaquest.features.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetHomeSummaryUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): LinguaQuestResult<HomeSummary, LinguaQuestDataError> =
        homeRepository.getHomeSummary()
}