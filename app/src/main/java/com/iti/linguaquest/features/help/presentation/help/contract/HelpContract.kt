package com.iti.linguaquest.features.help.presentation.help.contract

import com.iti.linguaquest.features.help.data.HelpTopic

data class HelpState(
    val selectedTopic: HelpTopic = HelpTopic.FAQS
)

sealed interface HelpIntent {
    data class OnTopicSelected(val topic: HelpTopic) : HelpIntent
    data object OnBackClicked : HelpIntent
}

sealed interface HelpEffect {
    data object NavigateBack : HelpEffect
    data object NavigateToFaqs : HelpEffect
    data object NavigateToContactUs : HelpEffect
    data object NavigateToUserGuide : HelpEffect
}
