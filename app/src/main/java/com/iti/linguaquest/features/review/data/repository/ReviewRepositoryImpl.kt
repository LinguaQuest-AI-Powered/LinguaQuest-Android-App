package com.iti.linguaquest.features.review.data.repository

import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.review.data.datasource.ReviewRemoteDataSource
import com.iti.linguaquest.features.review.domain.repository.ReviewRepository
import com.iti.linguaquest.features.review.domain.model.AIReviewResponse
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val remoteDataSource: ReviewRemoteDataSource
) : ReviewRepository {

    override suspend fun getAIReview(
        word: WordEntity
    ): LinguaQuestResult<AIReviewResponse, LinguaQuestDataError> =
        remoteDataSource.getAIReview(word)
}
