package com.iti.linguaquest.features.lockscreen.presentation.contract

import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord

data class LockScreenWordDetailState(
    val isLoading: Boolean = true,
    val words: List<LockScreenWord> = emptyList(),
    val highlightedWordId: Int? = null,
    val errorMessage: String? = null,
    val walletCoins: Int = 0
)
