package com.iti.linguaquest.features.mindreader.data.repository

import com.iti.linguaquest.features.mindreader.data.datasource.MindReaderDataSource
import com.iti.linguaquest.features.mindreader.data.datasource.remote.MindReaderAiService
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderDataset
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAiNextTurn
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAiQuizChoice
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAiHonestyResult
import com.iti.linguaquest.features.mindreader.domain.repository.MindReaderRepository
import javax.inject.Inject

class MindReaderRepositoryImpl @Inject constructor(
    private val dataSource: MindReaderDataSource,
    private val aiService: MindReaderAiService
) : MindReaderRepository {

    override suspend fun loadDataset(): MindReaderDataset {
        return dataSource.loadDataset()
    }

    override suspend fun getNextTurn(
        categoryContext: String,
        targetLanguage: String,
        nativeLanguage: String,
        historyPrompt: String
    ): MindReaderAiNextTurn {
        val aiResponse = aiService.getNextTurn(
            categoryContext = categoryContext,
            targetLanguage = targetLanguage,
            nativeLanguage = nativeLanguage,
            historyPrompt = historyPrompt
        ) ?: return MindReaderAiNextTurn.Error

        return if (aiResponse.type == "guess" || aiResponse.guessWord != null) {
            MindReaderAiNextTurn.Guess(
                word = aiResponse.guessWord ?: "",
                translation = aiResponse.guessTranslation ?: "",
                emoji = aiResponse.guessEmoji ?: "🤔"
            )
        } else {
            MindReaderAiNextTurn.Question(
                targetText = aiResponse.questionTargetText ?: "",
                nativeText = aiResponse.questionNativeText ?: ""
            )
        }
    }

    override suspend fun generateQuizChoices(
        categoryContext: String,
        correctWord: String,
        nativeLanguage: String,
        targetLanguage: String
    ): List<MindReaderAiQuizChoice>? {
        val response = aiService.generateQuizChoices(
            categoryContext = categoryContext,
            correctWord = correctWord,
            nativeLanguage = nativeLanguage,
            targetLanguage = targetLanguage
        ) ?: return null

        return response.choices.map {
            MindReaderAiQuizChoice(
                translationText = it.translationText,
                isCorrect = it.isCorrect
            )
        }
    }

    override suspend fun verifyUserWord(
        categoryContext: String,
        historyPrompt: String,
        claimedWord: String,
        feedbackLanguage: String
    ): MindReaderAiHonestyResult? {
        val response = aiService.verifyUserWord(
            categoryContext = categoryContext,
            historyPrompt = historyPrompt,
            claimedWord = claimedWord,
            feedbackLanguage = feedbackLanguage
        ) ?: return null

        return MindReaderAiHonestyResult(
            isHonest = response.isHonest,
            explanation = response.explanation
        )
    }
}
