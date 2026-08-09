package com.iti.linguaquest.features.notification.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.features.notification.domain.model.Notification
import com.iti.linguaquest.features.notification.presentation.contract.NotificationIntent
import com.iti.linguaquest.features.notification.presentation.contract.NotificationState
import com.iti.linguaquest.features.notification.presentation.view.components.NotificationDialogs
import com.iti.linguaquest.features.notification.presentation.view.components.NotificationListContainer

@Composable
fun NotificationContent(
    state: NotificationState,
    notifications: List<Notification>,
    modifier: Modifier = Modifier,
    onDeleteAllClick: (Rect) -> Unit = {},
    onDeleteNotificationClick: (Long, Rect) -> Unit = { _, _ -> },
    onCardClick: (Notification, Rect) -> Unit = { _, _ -> },
    onIntent: (NotificationIntent) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var deleteAllBounds by remember { mutableStateOf(Rect.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp, bottom = 24.dp)
        ) {
            LinguaQuestScreenTopBar(
                title = stringResource(id = R.string.notifications_title),
                onBackClicked = onBackClick,
                trailingContent = {
                    if (notifications.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(24.dp))
                            Text(
                                text = stringResource(R.string.delete_all),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .onGloballyPositioned { coordinates ->
                                        deleteAllBounds = coordinates.boundsInRoot()
                                    }
                                    .clickable { onDeleteAllClick(deleteAllBounds) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            NotificationListContainer(
                notifications = notifications,
                isLoading = state.isLoading,
                onCardClick = onCardClick,
                onDeleteClick = onDeleteNotificationClick
            )
        }

        NotificationDialogs(
            state = state,
            onIntent = onIntent
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationContentEmptyPreview() {
    LinguaQuestTheme {
        NotificationContent(
            state = NotificationState(),
            notifications = emptyList()
        )
    }
}
