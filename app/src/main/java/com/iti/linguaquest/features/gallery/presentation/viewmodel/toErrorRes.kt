package com.iti.linguaquest.features.gallery.presentation.viewmodel

import com.iti.linguaquest.R
import com.iti.linguaquest.core.result.LinguaQuestDataError

fun LinguaQuestDataError.toErrorRes(): Int = when (this) {
        LinguaQuestDataError.Remote.NO_INTERNET -> R.string.error_no_internet
        LinguaQuestDataError.Remote.REQUEST_TIMEOUT -> R.string.error_timeout
        LinguaQuestDataError.Remote.SERVER -> R.string.error_server
        LinguaQuestDataError.Remote.UNAUTHORIZED -> R.string.error_unauthorized
        LinguaQuestDataError.Remote.BAD_REQUEST -> R.string.error_bad_request
        LinguaQuestDataError.Remote.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
        else -> R.string.general_error
    }
