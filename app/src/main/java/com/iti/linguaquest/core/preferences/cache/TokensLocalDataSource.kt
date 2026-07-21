package com.iti.linguaquest.core.preferences.cache

import kotlinx.coroutines.flow.Flow

interface TokensLocalDataSource {
    val accessToken: Flow<String?>
    val refreshToken: Flow<String?>
    val isLoggedIn: Flow<Boolean>
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun clearTokens()
    fun getAccessTokenImmediate(): String?
}


