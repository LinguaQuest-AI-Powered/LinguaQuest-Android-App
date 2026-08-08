package com.iti.linguaquest.features.mindreader.presentation.contract

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderCategory
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderPopQuizChoice
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderPopQuizQuestion
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGuessResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameHistory

enum class MindReaderPhase {
    LOBBY,
    THINKING,
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
    val selectedCategory: MindReaderCategory? = null,
    val availableCategories: List<MindReaderCategory> = emptyList(),
    val coinBalance: Int = 0,
    val xpBalance: Int = 0,
    val maxQuestions: Int = 20,
    val currentQuestionNumber: Int = 0,
    val currentQuestionCandidate: com.iti.linguaquest.features.mindreader.domain.model.MindReaderQuestionCandidate? = null,
    val currentQuestion: String? = null,
    val translatedQuestion: String? = null,
    val showTranslation: Boolean = false,
    val targetLanguageCode: String = "en",
    val nativeLanguageCode: String = "en",
    val guessResult: MindReaderGuessResult? = null,
    val popQuizQuestion: MindReaderPopQuizQuestion? = null,
    val stumpInputValue: String = "",
    val resultInfo: MindReaderResultInfo? = null,
    val history: MindReaderGameHistory? = null
)
