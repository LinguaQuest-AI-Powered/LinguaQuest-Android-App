package com.iti.linguaquest.core.sharedComponents.text

import com.iti.linguaquest.R
import com.iti.linguaquest.core.result.LinguaQuestDataError

fun LinguaQuestDataError.toUiText(): UiText = when (this) {
    LinguaQuestDataError.Remote.NO_INTERNET -> UiText.StringResource(R.string.error_no_internet)
    LinguaQuestDataError.Remote.REQUEST_TIMEOUT -> UiText.StringResource(R.string.error_timeout)
    LinguaQuestDataError.Remote.SERVER -> UiText.StringResource(R.string.error_server)
    LinguaQuestDataError.Remote.BAD_REQUEST -> UiText.StringResource(R.string.error_bad_request)
    LinguaQuestDataError.Remote.UNAUTHORIZED -> UiText.StringResource(R.string.error_unauthorized)
    LinguaQuestDataError.Remote.TOO_MANY_REQUESTS -> UiText.StringResource(R.string.error_too_many_requests)
    LinguaQuestDataError.Remote.SERIALIZATION -> UiText.StringResource(R.string.error_generic)
    LinguaQuestDataError.Remote.EMPTY_RESULT -> UiText.StringResource(R.string.error_generic)
    LinguaQuestDataError.Remote.UNKNOWN -> UiText.StringResource(R.string.error_generic)
    is LinguaQuestDataError.CustomServerMessage -> UiText.DynamicString(message)

    is LinguaQuestDataError.Auth -> UiText.StringResource(R.string.error_generic)
    is LinguaQuestDataError.Firestore -> UiText.StringResource(R.string.error_generic)
    is LinguaQuestDataError.Local -> UiText.StringResource(R.string.error_generic)
}