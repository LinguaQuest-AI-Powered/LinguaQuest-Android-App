package com.iti.linguaquest.features.lockscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.connectivity.NetworkMonitor
import com.iti.linguaquest.features.lockscreen.domain.usecase.GenerateVocabularyBatchUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.GetLockScreenPostedOrOpenedWordsUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.MarkLockScreenWordOpenedUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.ObserveLockScreenPendingOnceUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenWordDetailIntent
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenWordDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
 @HiltViewModel

class LockScreenWordDetailViewModel @Inject constructor(
    private val markOpenedUseCase: MarkLockScreenWordOpenedUseCase,
    private val generateBatchUseCase: GenerateVocabularyBatchUseCase,
    private val getPostedOrOpenedWordsUseCase: GetLockScreenPostedOrOpenedWordsUseCase,
    private val observePendingOnceUseCase: ObserveLockScreenPendingOnceUseCase,
    private val networkMonitor: NetworkMonitor

 ) : ViewModel() {

     val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
         .stateIn(
             scope = viewModelScope,
             started = SharingStarted.WhileSubscribed(5_000),
             initialValue = true
         )


    private val _state = MutableStateFlow(LockScreenWordDetailState())
    val state: StateFlow<LockScreenWordDetailState> = _state.asStateFlow()

    init {
        observeWords()
    }

    fun setHighlightedWordId(wordId: Int?) {
        _state.update { it.copy(highlightedWordId = wordId) }
        if (wordId != null) {
            viewModelScope.launch {
                markOpenedUseCase(wordId)
            }
        }
    }

    fun onIntent(intent: LockScreenWordDetailIntent) {
        when (intent) {
            LockScreenWordDetailIntent.Load -> observeWords()
            LockScreenWordDetailIntent.Retry -> observeWords()
        }
    }

    private fun observeWords() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            getPostedOrOpenedWordsUseCase().collect { words ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        words = words,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun requestNewWord() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val pendingWord = observePendingOnceUseCase()
            if (pendingWord != null) {
                markOpenedUseCase(pendingWord.id)
                setHighlightedWordId(pendingWord.id)
            } else {
                 val generateResult = generateBatchUseCase()
                if (generateResult is LinguaQuestResult.Success) {
                    val newlyGeneratedWord = observePendingOnceUseCase()
                    if (newlyGeneratedWord != null) {
                        markOpenedUseCase(newlyGeneratedWord.id)
                        setHighlightedWordId(newlyGeneratedWord.id)
                    } else {
                        _state.update {
                            it.copy(isLoading = false, errorMessage = com.iti.linguaquest.R.string.lockscreen_error_generate_failed.toString())
                        }
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = com.iti.linguaquest.R.string.lockscreen_error_generate_failed.toString()
                        )
                    }
                }
            }
        }
    }
}
