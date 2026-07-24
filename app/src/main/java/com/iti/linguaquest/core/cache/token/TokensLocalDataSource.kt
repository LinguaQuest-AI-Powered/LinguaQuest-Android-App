package com.iti.linguaquest.core.cache.token

import kotlinx.coroutines.flow.Flow

interface TokensLocalDataSource {
    val accessToken: Flow<String?>
    val refreshToken: Flow<String?>
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun clearTokens()
    fun getAccessTokenImmediate(): String?
}

