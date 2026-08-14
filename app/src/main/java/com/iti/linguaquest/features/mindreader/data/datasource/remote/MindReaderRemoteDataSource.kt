package com.iti.linguaquest.features.mindreader.data.datasource.remote

import com.iti.linguaquest.features.mindreader.data.dto.MindReaderHonestyDto
import com.iti.linguaquest.features.mindreader.data.dto.MindReaderNextTurnDto
import com.iti.linguaquest.features.mindreader.data.dto.MindReaderQuizDto

interface MindReaderRemoteDataSource {
    suspend fun getNextTurn(prompt: String): MindReaderNextTurnDto?
    suspend fun generateQuizChoices(prompt: String): MindReaderQuizDto?
    suspend fun verifyUserWord(prompt: String): MindReaderHonestyDto?
}
