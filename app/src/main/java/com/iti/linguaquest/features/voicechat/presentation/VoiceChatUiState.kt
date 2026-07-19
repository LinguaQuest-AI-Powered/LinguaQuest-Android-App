package com.iti.linguaquest.features.voicechat.presentation

data class VoiceChatUiState(
    val isConnected: Boolean = false,
    val isRecording: Boolean = false,
    val transcript: String = "",
    val error: String? = null
)
