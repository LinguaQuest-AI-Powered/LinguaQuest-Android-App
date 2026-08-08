package com.iti.linguaquest.features.mindreader.domain.repository

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderDataset
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAiNextTurn
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAiQuizChoice
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAiHonestyResult

interface MindReaderRepository {
    suspend fun loadDataset(): MindReaderDataset

    suspend fun getNextTurn(
        categoryContext: String,
        targetLanguage: String,
        nativeLanguage: String,
        historyPrompt: String
    ): MindReaderAiNextTurn

    suspend fun generateQuizChoices(
        categoryContext: String,
        correctWord: String,
        nativeLanguage: String,
        targetLanguage: String
    ): List<MindReaderAiQuizChoice>?

    suspend fun verifyUserWord(
        categoryContext: String,
        historyPrompt: String,
        claimedWord: String,
        feedbackLanguage: String
    ): MindReaderAiHonestyResult?
}
