package com.iti.linguaquest.features.lockscreen.presentation.contract

sealed interface LockScreenWordDetailIntent {
    data object Load : LockScreenWordDetailIntent
    data object Retry : LockScreenWordDetailIntent
    data class SetHighlightedWordId(val wordId: Int?) : LockScreenWordDetailIntent
    data object RequestNewWord : LockScreenWordDetailIntent
}
