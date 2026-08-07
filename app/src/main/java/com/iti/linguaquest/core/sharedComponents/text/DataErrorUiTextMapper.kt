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
    LinguaQuestDataError.Remote.INSUFFICIENT_BALANCE -> UiText.StringResource(R.string.error_insufficient_balance)
    LinguaQuestDataError.Remote.SERIALIZATION -> UiText.StringResource(R.string.error_generic)
    LinguaQuestDataError.Remote.EMPTY_RESULT -> UiText.StringResource(R.string.error_generic)
    LinguaQuestDataError.Remote.UNKNOWN -> UiText.StringResource(R.string.error_generic)

    LinguaQuestDataError.Auth.INVALID_EMAIL -> UiText.StringResource(R.string.login_error_invalid_email)
    LinguaQuestDataError.Auth.INVALID_CREDENTIALS -> UiText.StringResource(R.string.login_error_invalid_credentials)
    LinguaQuestDataError.Auth.EMAIL_ALREADY_IN_USE, LinguaQuestDataError.Auth.EMAIL_ALREADY_EXISTS -> UiText.StringResource(R.string.error_email_already_in_use)
    LinguaQuestDataError.Auth.WEAK_PASSWORD -> UiText.StringResource(R.string.login_error_weak_password)
    LinguaQuestDataError.Auth.USER_DISABLED -> UiText.StringResource(R.string.login_error_user_disabled)
    LinguaQuestDataError.Auth.EMAIL_NOT_VERIFIED -> UiText.StringResource(R.string.login_error_email_not_verified)
    LinguaQuestDataError.Auth.TOKEN_NOT_VALID,
    LinguaQuestDataError.Auth.INVALID_REFRESH_TOKEN,
    LinguaQuestDataError.Auth.REFRESH_TOKEN_EXPIRED -> UiText.StringResource(R.string.login_error_token_not_valid)
    LinguaQuestDataError.Auth.EMAIL_NOT_FOUND -> UiText.StringResource(R.string.error_email_not_found)
    LinguaQuestDataError.Auth.USERNAME_ALREADY_EXISTS -> UiText.StringResource(R.string.error_username_already_exists)
    LinguaQuestDataError.Auth.INVALID_OTP -> UiText.StringResource(R.string.error_invalid_otp)
    LinguaQuestDataError.Auth.OTP_EXPIRED -> UiText.StringResource(R.string.error_otp_expired)
    LinguaQuestDataError.Auth.OTP_NOT_FOUND -> UiText.StringResource(R.string.error_otp_not_found)
    LinguaQuestDataError.Auth.TOO_MANY_REQUESTS -> UiText.StringResource(R.string.error_too_many_requests)
    LinguaQuestDataError.Auth.INTERNAL_SERVER_ERROR -> UiText.StringResource(R.string.error_server)
    is LinguaQuestDataError.Auth -> UiText.StringResource(R.string.error_generic)

    LinguaQuestDataError.Local.DISK_FULL -> UiText.StringResource(R.string.error_disk_full)
    is LinguaQuestDataError.Local -> UiText.StringResource(R.string.error_generic)
    is LinguaQuestDataError.Firestore -> UiText.StringResource(R.string.error_generic)
    is LinguaQuestDataError.CustomServerMessage -> UiText.DynamicString(message)
}
