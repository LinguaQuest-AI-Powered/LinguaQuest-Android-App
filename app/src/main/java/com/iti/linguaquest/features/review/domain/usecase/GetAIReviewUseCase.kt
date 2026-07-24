package com.iti.linguaquest.features.review.domain.usecase

import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.review.domain.model.AIReviewResponse
import com.iti.linguaquest.features.review.domain.repository.ReviewRepository
import javax.inject.Inject

class GetAIReviewUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(
        word: WordEntity
    ): LinguaQuestResult<AIReviewResponse, LinguaQuestDataError> =
        repository.getAIReview(word)
}
