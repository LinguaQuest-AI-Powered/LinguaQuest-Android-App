package com.iti.linguaquest.features.lockscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.lockscreen.domain.usecase.GetLockScreenWordByIdUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.MarkLockScreenWordOpenedUseCase
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenWordDetailIntent
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenWordDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockScreenWordDetailViewModel @Inject constructor(
    private val getWordByIdUseCase: GetLockScreenWordByIdUseCase,
    private val markOpenedUseCase: MarkLockScreenWordOpenedUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LockScreenWordDetailState())
    val state: StateFlow<LockScreenWordDetailState> = _state.asStateFlow()

    private var currentWordId: Int? = null

    fun setWordId(wordId: Int) {
        if (currentWordId == wordId) return
        currentWordId = wordId
        loadWord(wordId)
    }

    fun onIntent(intent: LockScreenWordDetailIntent) {
        when (intent) {
            LockScreenWordDetailIntent.Load -> currentWordId?.let { loadWord(it) }
            LockScreenWordDetailIntent.Retry -> currentWordId?.let { loadWord(it) }
        }
    }

    private fun loadWord(wordId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val word = getWordByIdUseCase(wordId)
            if (word == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Could not load this word."
                    )
                }
                return@launch
            }

            markOpenedUseCase(wordId)
            _state.update {
                it.copy(
                    isLoading = false,
                    word = word,
                    errorMessage = null
                )
            }
        }
    }
}
