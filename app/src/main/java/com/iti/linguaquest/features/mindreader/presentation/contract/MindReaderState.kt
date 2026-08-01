package com.iti.linguaquest.features.mindreader.presentation.contract

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderPopQuizChoice
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderPopQuizQuestion
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGuessResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameHistory

enum class MindReaderPhase {
    LOBBY,
    PLAYING,
    GUESSING_LOADING,
    GUESS_REVEAL,
    POP_QUIZ,
    STUMP,
    RESULT
}

enum class LingoEmotion {
    DEFAULT,
    PUZZLED,
    THINKING,
    EXCITED,
    ANGRY,
    CELEBRATING,
    DETECTIVE
}

data class MindReaderResultInfo(
    val isVictory: Boolean,
    val xpEarned: Int,
    val coinsEarned: Int,
    val reason: String? = null
)

data class MindReaderState(
    val isLoading: Boolean = false,
    val currentPhase: MindReaderPhase = MindReaderPhase.LOBBY,
    val lingoEmotion: LingoEmotion = LingoEmotion.DEFAULT,
    val selectedWorldId: Int? = null,
    
    // Core game state
    val coinBalance: Int = 0,
    val xpBalance: Int = 0,
    val maxQuestions: Int = 20,
    val currentQuestionNumber: Int = 0,
    
    // Playing phase state
    val currentQuestion: String? = null,
    val translatedQuestion: String? = null,
    val showTranslation: Boolean = false,
    val targetLanguageCode: String = "en",
    
    // Outcomes & special phases state
    val guessResult: MindReaderGuessResult? = null,
    val popQuizQuestion: MindReaderPopQuizQuestion? = null,
    val stumpCandidates: List<MindReaderEntity>? = null,
    val resultInfo: MindReaderResultInfo? = null,
    val history: MindReaderGameHistory? = null
)
