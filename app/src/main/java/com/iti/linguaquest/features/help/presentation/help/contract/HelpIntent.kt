package com.iti.linguaquest.features.help.presentation.help.contract

sealed interface HelpIntent {
    data class OnFaqToggled(val faqId: String) : HelpIntent
    data object OnContactSupportClicked : HelpIntent
    data object OnReportBugClicked : HelpIntent
    data object OnBackClicked : HelpIntent
}
