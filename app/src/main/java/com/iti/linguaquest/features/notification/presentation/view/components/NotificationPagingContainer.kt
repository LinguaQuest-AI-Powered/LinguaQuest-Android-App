package com.iti.linguaquest.features.notification.presentation.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.sharedComponents.LoadingView
import com.iti.linguaquest.features.notification.data.datasource.paging.NotificationPagingException
import com.iti.linguaquest.features.notification.domain.model.Notification
import com.iti.linguaquest.features.notification.presentation.contract.NotificationIntent


@Composable
fun NotificationPagingContainer(
    notifications: LazyPagingItems<Notification>?,
    onIntent: (NotificationIntent) -> Unit
) {
    if (notifications == null) {
        NotificationEmptyState()
        return
    }

    val refreshState = notifications.loadState.refresh
    val isEmptyResultError = (refreshState as? LoadState.Error)?.error is NotificationPagingException &&
            ((refreshState as? LoadState.Error)?.error as NotificationPagingException).error == LinguaQuestDataError.Remote.EMPTY_RESULT

    val isLoadingInitial = refreshState is LoadState.Loading && notifications.itemCount == 0
    val isEmpty = (notifications.itemCount == 0 && refreshState is LoadState.NotLoading) || isEmptyResultError
    val isErrorInitial = refreshState is LoadState.Error && notifications.itemCount == 0

    when {
        isLoadingInitial -> LoadingView(modifier = Modifier.fillMaxSize())
        isEmpty -> NotificationEmptyState()
        isErrorInitial -> NotificationErrorState(onRetry = { notifications.retry() })
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(
                    count = notifications.itemCount,
                    key = notifications.itemKey { it.id }
                ) { index ->
                    notifications[index]?.let { item ->
                        NotificationCard(
                            notification = item,
                            onCardClick = { onIntent(NotificationIntent.NotificationClicked(item)) },
                            onDeleteClick = { onIntent(NotificationIntent.DeleteNotificationClicked(item.id)) }
                        )
                    }
                }

                if (notifications.loadState.append is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}