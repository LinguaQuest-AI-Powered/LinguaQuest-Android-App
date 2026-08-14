package com.iti.linguaquest.core.sharedComponents.text

import com.iti.linguaquest.R
import retrofit2.HttpException

fun Throwable?.toAiErrorUiText(fallbackResId: Int = R.string.error_generic): UiText {
    if (this == null) return UiText.StringResource(fallbackResId)

    if (this is HttpException && this.code() == 429) {
        return UiText.StringResource(R.string.roleplay_error_quota_exceeded)
    }

    val message = this.message.orEmpty()
    return message.toAiErrorUiText(fallbackResId)
}

fun String?.toAiErrorUiText(fallbackResId: Int = R.string.error_generic): UiText {
    val message = this?.lowercase().orEmpty()

    if (message.isBlank()) {
        return UiText.StringResource(fallbackResId)
    }

    if (
        message.contains("429") ||
        message.contains("quota") ||
        message.contains("resource_exhausted") ||
        message.contains("rate limit") ||
        message.contains("rate_limit") ||
        message.contains("exceeded") ||
        message.contains("limit reached")
    ) {
        return UiText.StringResource(R.string.roleplay_error_quota_exceeded)
    }

    if (
        message.contains("goaway") ||
        message.contains("session ended") ||
        message.contains("session closed")
    ) {
        return UiText.StringResource(R.string.roleplay_error_session_ended)
    }

    if (
        message.contains("unable to resolve host") ||
        message.contains("failed to connect") ||
        message.contains("timeout") ||
        message.contains("network") ||
        message.contains("connection lost") ||
        message.contains("broken pipe") ||
        message.contains("socket") ||
        message.contains("end of stream") ||
        message.contains("unknown connection error")
    ) {
        return UiText.StringResource(R.string.roleplay_error_connection_lost)
    }

    return UiText.StringResource(fallbackResId)
}
