package com.iti.linguaquest.features.help.presentation.help.contract

import androidx.annotation.StringRes

data class HelpState(
    val faqItems: List<FaqItem> = emptyList(),
    val expandedFaqId: String? = null
)

data class FaqItem(
    val id: String,
    @StringRes val questionRes: Int,
    @StringRes val answerRes: Int
)
