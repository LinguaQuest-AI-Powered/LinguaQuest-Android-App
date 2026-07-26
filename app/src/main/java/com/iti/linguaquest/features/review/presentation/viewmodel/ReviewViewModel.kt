package com.iti.linguaquest.features.review.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.connectivity.NetworkMonitor
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.review.domain.usecase.GetAIReviewUseCase
import com.iti.linguaquest.features.review.presentation.contract.ReviewEffect
import com.iti.linguaquest.features.review.presentation.contract.ReviewIntent
import com.iti.linguaquest.features.review.presentation.contract.ReviewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val getAIReviewUseCase: GetAIReviewUseCase,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ReviewState())
    val state = _state.asStateFlow()

    private val _effects = Channel<ReviewEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()
    val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()

        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )
    fun onIntent(intent: ReviewIntent) {
        when (intent) {
            is ReviewIntent.LoadReview   -> loadReview(intent.word)
            ReviewIntent.RetryClicked    -> _state.value.word?.let { loadReview(it) }
            ReviewIntent.BackClicked     -> sendEffect(ReviewEffect.NavigateBack)
            is ReviewIntent.SpeakSection -> toggleSectionSpeak(intent)
        }
    }

    private fun loadReview(word: WordEntity) {
        viewModelScope.launch {
            _state.update { it.copy(word = word, isLoading = true, errorMessage = null, aiResponse = null) }

            when (val result = getAIReviewUseCase(word)) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isLoading = false, aiResponse = result.data) }
                }
                is LinguaQuestResult.Failure -> {
                    val msg = if (result.error is LinguaQuestDataError.CustomServerMessage) {
                        (result.error as  LinguaQuestDataError.CustomServerMessage).message
                    } else {
                        "Couldn't get AI review. Please try again."
                    }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = msg
                        )
                    }
                }
            }
        }
    }



     private fun toggleSectionSpeak(intent: ReviewIntent.SpeakSection) {
        val currentSection = _state.value.speakingSectionId

         if (currentSection == intent.sectionId) {
            _state.update { it.copy(speakingSectionId = null, isSpeaking = false) }
            sendEffect(ReviewEffect.StopSpeaking)
            return
        }
         _state.update { it.copy(speakingSectionId = intent.sectionId, isSpeaking = false) }
        sendEffect(ReviewEffect.SpeakText(
            text = intent.text,
            language = intent.language,
            sectionId = intent.sectionId
        ))
    }

    fun onSpeakingFinished() {
        _state.update { it.copy(isSpeaking = false, speakingSectionId = null) }
    }

    private fun sendEffect(effect: ReviewEffect) {
        viewModelScope.launch { _effects.send(effect) }
    }
}
