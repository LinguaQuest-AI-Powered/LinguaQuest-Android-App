package com.iti.linguaquest.features.notification.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.core.sharedComponents.offline.NoInternetMiniPopup
import com.iti.linguaquest.features.home.utils.calculatePopupOffset
import com.iti.linguaquest.features.notification.presentation.contract.NotificationIntent
import com.iti.linguaquest.features.notification.presentation.viewmodel.NotificationViewModel

@Composable
fun NotificationScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    var showOfflinePopup by remember { mutableStateOf(false) }
    var offlinePopupAnchor by remember { mutableStateOf<Rect?>(null) }
    var offlinePopupSize by remember { mutableStateOf(IntSize.Zero) }

    fun guardOnline(anchor: Rect? = null, action: () -> Unit) {
        if (isOnline) {
            action()
        } else {
            offlinePopupAnchor = anchor
            showOfflinePopup = true
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        NotificationContent(
            state = state,
            notifications = notifications,
            modifier = Modifier.fillMaxSize(),
            onDeleteAllClick = { anchor ->
                guardOnline(anchor) {
                    viewModel.onIntent(NotificationIntent.DeleteAllClicked)
                }
            },
            onDeleteNotificationClick = { id, anchor ->
                guardOnline(anchor) {
                    viewModel.onIntent(NotificationIntent.DeleteNotificationClicked(id))
                }
            },
            onCardClick = { notification, anchor ->
                if (!notification.isRead) {
                    guardOnline(anchor) {
                        viewModel.onIntent(NotificationIntent.NotificationClicked(notification))
                    }
                } else {
                    viewModel.onIntent(NotificationIntent.NotificationClicked(notification))
                }
            },
            onIntent = { intent -> viewModel.onIntent(intent) },
            onBackClick = onBackClick
        )

        if (showOfflinePopup) {
            val popupOffset = remember(
                offlinePopupAnchor,
                offlinePopupSize,
                configuration.screenWidthDp,
                configuration.screenHeightDp
            ) {
                calculatePopupOffset(
                    anchor = offlinePopupAnchor,
                    popupSize = offlinePopupSize,
                    screenWidthDp = configuration.screenWidthDp,
                    screenHeightDp = configuration.screenHeightDp,
                    density = density
                )
            }

            NoInternetMiniPopup(
                isOnline = isOnline,
                modifier = Modifier
                    .offset { popupOffset }
                    .onSizeChanged { offlinePopupSize = it },
                onDismiss = {
                    showOfflinePopup = false
                    offlinePopupAnchor = null
                }
            )
        }
    }
}
