package com.iti.linguaquest.features.notification.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.iti.linguaquest.R
import com.iti.linguaquest.features.notification.domain.model.Notification
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ShareTopBar
import com.iti.linguaquest.features.notification.presentation.contract.NotificationIntent
import com.iti.linguaquest.features.notification.presentation.contract.NotificationState
import com.iti.linguaquest.features.notification.presentation.view.components.NotificationDialogs
import com.iti.linguaquest.features.notification.presentation.view.components.NotificationPagingContainer


@Composable
fun NotificationContent(
    state: NotificationState,
    modifier: Modifier = Modifier,
    notifications: LazyPagingItems<Notification>? = null,
    onIntent: (NotificationIntent) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp)
                .statusBarsPadding()
        ) {
            ShareTopBar(
                title = R.string.notifications_title,
                onBackClick = onBackClick,
                trailingContent = {
                    if (notifications != null && notifications.itemCount > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(24.dp))
                            Text(
                                text = stringResource(R.string.delete_all),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .clickable { onIntent(NotificationIntent.DeleteAllClicked) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            NotificationPagingContainer(
                notifications = notifications,
                onIntent = onIntent
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
            state = NotificationState()
        )
    }
}