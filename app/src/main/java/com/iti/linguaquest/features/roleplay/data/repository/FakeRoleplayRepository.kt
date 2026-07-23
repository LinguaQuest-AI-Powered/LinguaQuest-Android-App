package com.iti.linguaquest.features.roleplay.data.repository

import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeRoleplayRepository @Inject constructor() : RoleplayRepository {

    private val _events = MutableSharedFlow<RoleplayLiveEvent>()
    override val events: Flow<RoleplayLiveEvent> = _events

    override suspend fun connect(systemPrompt: String) {
        // Fake connection logic
    }

    override suspend fun disconnect() {
        // Fake disconnect logic
    }

    override fun startMicrophone() {
        // Fake mic start
    }

    override fun stopMicrophone() {
        // Fake mic stop
    }
}
