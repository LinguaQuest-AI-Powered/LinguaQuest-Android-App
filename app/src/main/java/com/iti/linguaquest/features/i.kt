package com.iti.linguaquest.features

import com.iti.linguaquest.core.connectivity.NetworkMonitor

/*
auth
onboarding
setting
edit
Review
profile
AchievementScreen
LeaderboardScreen

===================
private val networkMonitor: NetworkMonitor
  val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

            val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    OfflineAwareContent(isOnline = isOnline) {

*/