package com.iti.linguaquest.features.help.presentation.help.contract


sealed interface HelpEffect {
    data object NavigateBack : HelpEffect
    data object NavigateToContactSupport : HelpEffect
    data object NavigateToReportBug : HelpEffect
}
