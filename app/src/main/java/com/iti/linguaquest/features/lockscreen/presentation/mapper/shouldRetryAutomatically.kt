package com.iti.linguaquest.features.lockscreen.presentation.mapper

import com.iti.linguaquest.R
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.sharedComponents.text.UiText

fun LinguaQuestDataError.shouldRetryAutomatically(): Boolean {
    return when (this) {
        LinguaQuestDataError.Remote.REQUEST_TIMEOUT,
        LinguaQuestDataError.Remote.NO_INTERNET,
        LinguaQuestDataError.Remote.TOO_MANY_REQUESTS,
        LinguaQuestDataError.Remote.SERVER -> true

        LinguaQuestDataError.Remote.BAD_REQUEST,
        LinguaQuestDataError.Remote.UNAUTHORIZED,
        LinguaQuestDataError.Remote.SERIALIZATION,
        LinguaQuestDataError.Remote.EMPTY_RESULT,
        LinguaQuestDataError.Remote.UNKNOWN,
        LinguaQuestDataError.Local.NOT_FOUND,
        LinguaQuestDataError.Local.DISK_FULL,
        LinguaQuestDataError.Local.CONSTRAINT_VIOLATION,
        LinguaQuestDataError.Local.UNKNOWN,
        is LinguaQuestDataError.CustomServerMessage -> false

        else -> false
    }
}

fun LinguaQuestDataError.toUiText(shouldRetry: Boolean = this.shouldRetryAutomatically()): UiText {
    return when (this) {
        LinguaQuestDataError.Local.NOT_FOUND ->
            UiText.StringResource(R.string.lockscreen_required_inputs_error)

        LinguaQuestDataError.Remote.NO_INTERNET,
        LinguaQuestDataError.Remote.REQUEST_TIMEOUT,
        LinguaQuestDataError.Remote.TOO_MANY_REQUESTS,
        LinguaQuestDataError.Remote.SERVER ->
            if (shouldRetry) {
                UiText.StringResource(R.string.lockscreen_error_no_internet_retry)
            } else {
                UiText.StringResource(R.string.lockscreen_error_no_internet)
            }

        LinguaQuestDataError.Remote.SERIALIZATION,
        LinguaQuestDataError.Remote.EMPTY_RESULT ->
            UiText.StringResource(R.string.lockscreen_error_bad_format)

        LinguaQuestDataError.Remote.BAD_REQUEST,
        LinguaQuestDataError.Remote.UNAUTHORIZED,
        LinguaQuestDataError.Remote.UNKNOWN,
        LinguaQuestDataError.Local.DISK_FULL,
        LinguaQuestDataError.Local.CONSTRAINT_VIOLATION,
        LinguaQuestDataError.Local.UNKNOWN ->
            UiText.StringResource(R.string.lockscreen_enable_error)

        is LinguaQuestDataError.CustomServerMessage ->
            if (message.isNotBlank()) {
                UiText.DynamicString(message)
            } else {
                UiText.StringResource(R.string.lockscreen_enable_error)
            }

        else -> UiText.StringResource(R.string.lockscreen_enable_error)
    }
}