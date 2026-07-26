package com.iti.linguaquest.features.roleplay.domain.usecase

import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import com.iti.linguaquest.features.roleplay.domain.repository.RoleplayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLiveEventsUseCase @Inject constructor(
    private val repository: RoleplayRepository
) {
    operator fun invoke(): Flow<RoleplayLiveEvent> {
        return repository.events
    }
}
