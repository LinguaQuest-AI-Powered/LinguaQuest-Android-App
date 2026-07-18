package com.iti.linguaquest.features.review.domain.repository

import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.review.domain.model.AIReviewResponse

interface ReviewRepository {
    suspend fun getAIReview(word: WordEntity): LinguaQuestResult<AIReviewResponse, LinguaQuestDataError>
}
