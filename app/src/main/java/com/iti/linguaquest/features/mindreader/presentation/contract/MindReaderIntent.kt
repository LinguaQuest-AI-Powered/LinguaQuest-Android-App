package com.iti.linguaquest.features.mindreader.presentation.contract

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderCategory
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderPopQuizChoice

sealed interface MindReaderIntent {
    data object StartGameClicked : MindReaderIntent
    data class CategorySelected(val category: MindReaderCategory) : MindReaderIntent
    data class AnswerClicked(val answer: MindReaderAnswerOption) : MindReaderIntent
    data object TranslateClicked : MindReaderIntent
    data object GuessVerifiedCorrect : MindReaderIntent
    data object GuessVerifiedIncorrect : MindReaderIntent
    data class PopQuizAnswered(val choice: MindReaderPopQuizChoice) : MindReaderIntent
    data class StumpWordSelected(val entity: MindReaderEntity) : MindReaderIntent
    data object PlayAudioClicked : MindReaderIntent
    data object PlayGuessAudioClicked : MindReaderIntent
    data object TryAgainClicked : MindReaderIntent
    data object ReturnToHomeClicked : MindReaderIntent
}
