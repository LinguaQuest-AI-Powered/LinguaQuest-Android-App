package com.iti.linguaquest.core.cache

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class TokensLocalDataSourceImpl @Inject constructor(
    private val encryptedSharedPreferences: SharedPreferences
) : TokensLocalDataSource {

    private val _accessToken =
        MutableStateFlow(encryptedSharedPreferences.getString(TokenKeys.ACCESS_TOKEN, null))
    override val accessToken: Flow<String?> = _accessToken.asStateFlow()

    private val _isLoggedIn =
        MutableStateFlow(encryptedSharedPreferences.getBoolean(TokenKeys.IS_LOGGED_IN, false))
    override val isLoggedIn: Flow<Boolean> = _isLoggedIn.asStateFlow()

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        encryptedSharedPreferences.edit().apply {
            putString(TokenKeys.ACCESS_TOKEN, accessToken)
            putString(TokenKeys.REFRESH_TOKEN, refreshToken)
            putBoolean(TokenKeys.IS_LOGGED_IN, true)
            apply()
        }
        _accessToken.value = accessToken
        _isLoggedIn.value = true
    }

    override suspend fun clearTokens() {
        encryptedSharedPreferences.edit().apply {
            remove(TokenKeys.ACCESS_TOKEN)
            remove(TokenKeys.REFRESH_TOKEN)
            putBoolean(TokenKeys.IS_LOGGED_IN, false)
            apply()
        }
        _accessToken.value = null
        _isLoggedIn.value = false
    }

    override fun getAccessTokenImmediate(): String? {
        return encryptedSharedPreferences.getString(TokenKeys.ACCESS_TOKEN, null)
    }
}