package com.iti.linguaquest.core.cache.data.repository

import com.iti.linguaquest.core.cache.data.datasource.SessionManagerDataSource
import com.iti.linguaquest.core.cache.domain.repository.SessionManagerRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SessionManagerRepositoryImpl @Inject constructor(
    private val sessionManagerDataSource: SessionManagerDataSource
) : SessionManagerRepository {

    override val isLoggedIn: Flow<Boolean> = sessionManagerDataSource.isLoggedIn

    override val firstTime: Flow<Boolean> = sessionManagerDataSource.firstTime

    override suspend fun saveIsLoggedIn(isLoggedIn: Boolean) {
        sessionManagerDataSource.saveIsLoggedIn(isLoggedIn)
    }

    override suspend fun saveFirstTime(firstTime: Boolean) {
        sessionManagerDataSource.saveFirstTime(firstTime)
    }
}
