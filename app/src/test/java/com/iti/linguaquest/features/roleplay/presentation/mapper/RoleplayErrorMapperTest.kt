package com.iti.linguaquest.features.roleplay.presentation.mapper

import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toAiErrorUiText
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class RoleplayErrorMapperTest {

    @Test
    fun toAiErrorUiText_returnsFallback_whenThrowableIsNull() {
        // Given
        val error: Throwable? = null

        // When
        val result = error.toAiErrorUiText(fallbackResId = R.string.roleplay_error_failed_connect)

        // Then
        assertEquals(UiText.StringResource(R.string.roleplay_error_failed_connect), result)
    }

    @Test
    fun toAiErrorUiText_returnsQuotaExceeded_whenHttpExceptionIs429() {
        // Given
        val response = Response.error<Any>(429, "".toResponseBody(null))
        val exception = HttpException(response)

        // When
        val result = exception.toAiErrorUiText()

        // Then
        assertEquals(UiText.StringResource(R.string.roleplay_error_quota_exceeded), result)
    }

    @Test
    fun toAiErrorUiText_returnsQuotaExceeded_whenMessageContainsQuotaKeywords() {
        // Given
        val keywords = listOf(
            "HTTP 429 error",
            "quota exceeded for model",
            "RESOURCE_EXHAUSTED",
            "Rate limit exceeded",
            "rate_limit reached",
            "exceeded daily quota",
            "limit reached for user"
        )

        keywords.forEach { keyword ->
            // When
            val result = Exception(keyword).toAiErrorUiText()

            // Then
            assertEquals(UiText.StringResource(R.string.roleplay_error_quota_exceeded), result)
        }
    }

    @Test
    fun toAiErrorUiText_returnsSessionEnded_whenMessageContainsSessionKeywords() {
        // Given
        val keywords = listOf(
            "server sent GOAWAY frame",
            "session ended by server",
            "session closed unexpectedly"
        )

        keywords.forEach { keyword ->
            // When
            val result = Exception(keyword).toAiErrorUiText()

            // Then
            assertEquals(UiText.StringResource(R.string.roleplay_error_session_ended), result)
        }
    }

    @Test
    fun toAiErrorUiText_returnsConnectionLost_whenMessageContainsNetworkKeywords() {
        // Given
        val keywords = listOf(
            "unable to resolve host api.gemini.com",
            "failed to connect to server",
            "timeout during handshake",
            "network error occurred",
            "connection lost during call",
            "broken pipe write failed",
            "socket closed",
            "end of stream reached",
            "unknown connection error"
        )

        keywords.forEach { keyword ->
            // When
            val result = Exception(keyword).toAiErrorUiText()

            // Then
            assertEquals(UiText.StringResource(R.string.roleplay_error_connection_lost), result)
        }
    }

    @Test
    fun toAiErrorUiText_returnsFallback_whenMessageIsGenericOrBlank() {
        // Given
        val genericException = Exception("Unexpected parsing bug")
        val blankException = Exception("   ")

        // When
        val genericResult = genericException.toAiErrorUiText(fallbackResId = R.string.roleplay_error_evaluation_failed)
        val blankResult = blankException.toAiErrorUiText(fallbackResId = R.string.roleplay_error_generic)

        // Then
        assertEquals(UiText.StringResource(R.string.roleplay_error_evaluation_failed), genericResult)
        assertEquals(UiText.StringResource(R.string.roleplay_error_generic), blankResult)
    }

    @Test
    fun stringToAiErrorUiText_returnsFallback_whenStringIsNull() {
        // Given
        val text: String? = null

        // When
        val result = text.toAiErrorUiText(fallbackResId = R.string.roleplay_error_connection_lost)

        // Then
        assertEquals(UiText.StringResource(R.string.roleplay_error_connection_lost), result)
    }

    @Test
    fun stringToAiErrorUiText_returnsQuotaExceeded_whenStringContainsKeywords() {
        // Given
        val text = "Error: RESOURCE_EXHAUSTED from backend"

        // When
        val result = text.toAiErrorUiText()

        // Then
        assertEquals(UiText.StringResource(R.string.roleplay_error_quota_exceeded), result)
    }

    @Test
    fun stringToAiErrorUiText_returnsSessionEnded_whenStringContainsKeywords() {
        // Given
        val text = "Live session closed"

        // When
        val result = text.toAiErrorUiText()

        // Then
        assertEquals(UiText.StringResource(R.string.roleplay_error_session_ended), result)
    }

    @Test
    fun stringToAiErrorUiText_returnsConnectionLost_whenStringContainsKeywords() {
        // Given
        val text = "Failed to connect to host"

        // When
        val result = text.toAiErrorUiText()

        // Then
        assertEquals(UiText.StringResource(R.string.roleplay_error_connection_lost), result)
    }
}
