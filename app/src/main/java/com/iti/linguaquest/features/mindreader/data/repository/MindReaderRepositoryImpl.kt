package com.iti.linguaquest.features.mindreader.data.repository

import com.iti.linguaquest.features.mindreader.data.datasource.local.MindReaderLocalDataSource
import com.iti.linguaquest.features.mindreader.data.datasource.remote.MindReaderRemoteDataSource
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAiHonestyResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAiNextTurn
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAiQuizChoice
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderCategory
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameConfig
import com.iti.linguaquest.features.mindreader.domain.prompt.MindReaderPromptFactory
import com.iti.linguaquest.features.mindreader.domain.repository.MindReaderRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MindReaderRepositoryImpl @Inject constructor(
    private val localDataSource: MindReaderLocalDataSource,
    private val remoteDataSource: MindReaderRemoteDataSource
) : MindReaderRepository {

    override suspend fun getCategories(): List<MindReaderCategory> {
        val dtoList = localDataSource.getCategories()
        return dtoList.map { dto ->
            MindReaderCategory(
                id = dto.id,
                displayName = dto.displayName,
                emoji = dto.emoji
            )
        }
    }

    override suspend fun getGameConfig(): MindReaderGameConfig {
        return localDataSource.getGameConfig()
    }

    override suspend fun getNextTurn(
        categoryContext: String,
        targetLanguage: String,
        nativeLanguage: String,
        historyPrompt: String
    ): MindReaderAiNextTurn {
        val prompt = MindReaderPromptFactory.createNextTurnPrompt(
            categoryContext = categoryContext,
            targetLanguage = targetLanguage,
            nativeLanguage = nativeLanguage,
            historyPrompt = historyPrompt
        )
        val dto = remoteDataSource.getNextTurn(prompt) ?: return MindReaderAiNextTurn.Error

        return if (dto.type == "guess" || dto.guessWord != null) {
            MindReaderAiNextTurn.Guess(
                word = dto.guessWord ?: "",
                translation = dto.guessTranslation ?: "",
                emoji = dto.guessEmoji ?: "🤔"
            )
        } else {
            MindReaderAiNextTurn.Question(
                targetText = dto.questionTargetText ?: "",
                nativeText = dto.questionNativeText ?: ""
            )
        }
    }

    override suspend fun generateQuizChoices(
        categoryContext: String,
        correctWord: String,
        nativeLanguage: String,
        targetLanguage: String
    ): List<MindReaderAiQuizChoice>? {
        val prompt = MindReaderPromptFactory.createQuizChoicesPrompt(
            categoryContext = categoryContext,
            correctWord = correctWord,
            nativeLanguage = nativeLanguage,
            targetLanguage = targetLanguage
        )
        val response = remoteDataSource.generateQuizChoices(prompt) ?: return null

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
        val prompt = MindReaderPromptFactory.createHonestyVerificationPrompt(
            categoryContext = categoryContext,
            historyPrompt = historyPrompt,
            claimedWord = claimedWord,
            feedbackLanguage = feedbackLanguage
        )
        val response = remoteDataSource.verifyUserWord(prompt) ?: return null

        return MindReaderAiHonestyResult(
            isHonest = response.isHonest,
            explanation = response.explanation
        )
    }
}
