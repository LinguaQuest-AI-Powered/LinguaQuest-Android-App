package com.iti.linguaquest.core.cache.domain.repository

import kotlinx.coroutines.flow.Flow

interface SessionManagerRepository {
    val isLoggedIn: Flow<Boolean>
    val firstTime: Flow<Boolean>

    suspend fun saveIsLoggedIn(isLoggedIn: Boolean)
    suspend fun saveFirstTime(firstTime: Boolean)
    suspend fun clearLanguageDependentData()
}
