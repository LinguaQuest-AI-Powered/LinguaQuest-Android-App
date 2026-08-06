package com.iti.linguaquest.core.session

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed interface SessionEvent {
    data object SessionExpired : SessionEvent
    data object LoggedOut : SessionEvent
}

interface SessionEventBus {
    val events: Flow<SessionEvent>
    suspend fun emit(event: SessionEvent)
}

@Singleton
class SessionEventBusImpl @Inject constructor() : SessionEventBus {
    private val _events = MutableSharedFlow<SessionEvent>(extraBufferCapacity = 1)
    override val events: Flow<SessionEvent> = _events.asSharedFlow()

    override suspend fun emit(event: SessionEvent) {
        _events.emit(event)
    }
}
