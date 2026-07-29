package com.iti.linguaquest.features.help.presentation.faqs.contract

import com.iti.linguaquest.features.help.data.FaqItem
import com.iti.linguaquest.features.help.data.FaqsDefaults

data class FaqsState(
    val items: List<FaqItem> = FaqsDefaults.items
)

sealed interface FaqsIntent {
    data object OnBackClicked : FaqsIntent
}

sealed interface FaqsEffect {
    data object NavigateBack : FaqsEffect
}
