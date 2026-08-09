package com.iti.linguaquest.features.mindreader.data.datasource.remote

import com.iti.linguaquest.features.mindreader.data.datasource.remote.mdoel.MindReaderHonestyResponse
import com.iti.linguaquest.features.mindreader.data.datasource.remote.mdoel.MindReaderNextStepResponse
import com.iti.linguaquest.features.mindreader.data.datasource.remote.mdoel.MindReaderQuizResponse

interface MindReaderAiService {

    suspend fun getNextTurn(
        categoryContext: String,
        targetLanguage: String,
        nativeLanguage: String,
        historyPrompt: String
    ): MindReaderNextStepResponse?

    suspend fun generateQuizChoices(
        categoryContext: String,
        correctWord: String,
        nativeLanguage: String,
        targetLanguage: String
    ): MindReaderQuizResponse?

    suspend fun verifyUserWord(
        categoryContext: String,
        historyPrompt: String,
        claimedWord: String,
        feedbackLanguage: String
    ): MindReaderHonestyResponse?
}