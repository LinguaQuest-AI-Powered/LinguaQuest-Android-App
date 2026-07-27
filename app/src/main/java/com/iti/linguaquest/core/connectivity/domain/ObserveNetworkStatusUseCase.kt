package com.iti.linguaquest.core.connectivity.domain

import com.iti.linguaquest.core.connectivity.NetworkMonitor
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveNetworkStatusUseCase @Inject constructor(
    private val networkMonitor: NetworkMonitor
) {
    operator fun invoke(): Flow<Boolean> = networkMonitor.isOnline
}