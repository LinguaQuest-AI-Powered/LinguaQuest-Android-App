package com.iti.linguaquest.features.voicechat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.voicechat.domain.model.VoiceChatEvent
import com.iti.linguaquest.features.voicechat.domain.repository.VoiceChatRepository
import com.iti.linguaquest.features.voicechat.domain.usecase.ConnectVoiceChatUseCase
import com.iti.linguaquest.features.voicechat.domain.usecase.ObserveVoiceChatEventsUseCase
import com.iti.linguaquest.features.voicechat.domain.usecase.SendVoiceTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoiceChatViewModel @Inject constructor(
    private val connect: ConnectVoiceChatUseCase,
    private val sendText: SendVoiceTextUseCase,
    private val observeEvents: ObserveVoiceChatEventsUseCase,
    private val repository: VoiceChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VoiceChatUiState())
    val uiState: StateFlow<VoiceChatUiState> = _uiState

    init {
        observeEvents().onEach { event ->
            when (event) {
                is VoiceChatEvent.TextChunk ->
                    _uiState.update { it.copy(transcript = it.transcript + event.text) }

                is VoiceChatEvent.Error ->
                    _uiState.update { it.copy(error = event.message) }

                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    fun onConnectClicked() = viewModelScope.launch {
        connect()
        _uiState.update { it.copy(isConnected = true) }
        sendText("""
    Please introduce yourself.
    Your name is Lingo, the AI assistant inside the LinguaQuest app.
    Say hello, introduce yourself briefly, and explain that you help users learn languages, practice conversations, translate text, explain grammar, and improve pronunciation.
    """.trimIndent()
        )
    }

    fun onMicClicked() {
        repository.startListening()
        _uiState.update { it.copy(isRecording = true) }
    }

    fun onStopClicked() {
        repository.stopListening()
        _uiState.update { it.copy(isRecording = false) }
    }

    override fun onCleared() {
        super.onCleared()
        repository.disconnect()
    }
}
