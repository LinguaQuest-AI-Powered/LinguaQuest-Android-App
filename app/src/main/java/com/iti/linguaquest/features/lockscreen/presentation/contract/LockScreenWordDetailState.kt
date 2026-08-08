package com.iti.linguaquest.features.lockscreen.presentation.contract

import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import com.iti.linguaquest.core.sharedComponents.text.UiText

data class LockScreenWordDetailState(
    val isLoading: Boolean = true,
    val words: List<LockScreenWord> = emptyList(),
    val highlightedWordId: Int? = null,
    val errorMessage: UiText? = null
)
