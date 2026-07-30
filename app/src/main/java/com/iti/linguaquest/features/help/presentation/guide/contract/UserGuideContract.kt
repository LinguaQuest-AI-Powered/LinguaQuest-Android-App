package com.iti.linguaquest.features.help.presentation.guide.contract

import com.iti.linguaquest.features.help.data.UserGuideDefaults
import com.iti.linguaquest.features.help.data.UserGuideSection

data class UserGuideState(
    val sections: List<UserGuideSection> = UserGuideDefaults.sections
)

sealed interface UserGuideIntent {
    data object OnBackClicked : UserGuideIntent
}

sealed interface UserGuideEffect {
    data object NavigateBack : UserGuideEffect
}
