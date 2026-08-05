package com.iti.linguaquest.features.notification.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.iti.linguaquest.features.notification.presentation.contract.NotificationEffect
import com.iti.linguaquest.features.notification.presentation.viewmodel.NotificationViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NotificationScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val notifications = viewModel.notifications.collectAsLazyPagingItems()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is NotificationEffect.RefreshNotifications -> {
                    notifications.refresh()
                }
            }
        }
    }

    NotificationContent(
        modifier = modifier,
        state = state,
        notifications = notifications,
        onIntent = { intent -> viewModel.onIntent(intent) },
        onBackClick = onBackClick
    )
}
