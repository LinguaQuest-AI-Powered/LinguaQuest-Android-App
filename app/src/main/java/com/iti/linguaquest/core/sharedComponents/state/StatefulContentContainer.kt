package com.iti.linguaquest.core.sharedComponents.state

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iti.linguaquest.core.sharedComponents.ErrorView
import com.iti.linguaquest.core.sharedComponents.LoadingView
import com.iti.linguaquest.core.sharedComponents.text.UiText

@Composable
fun StatefulContentContainer(
    dataStatus: DataStatus,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    onErrorDismiss: (() -> Unit)? = null,
    onRefresh: (() -> Unit)? = null,
    loadingContent: @Composable () -> Unit = {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingView()
        }
    },
    errorContent: @Composable (UiText, (() -> Unit)?) -> Unit = { message, onDismiss ->
        ErrorView(
            message = message,
            onRetry = onRetry,
            onDismissRequest = onDismiss,
            modifier = Modifier.fillMaxSize()
        )
    },
    content: @Composable () -> Unit
) {
    val layoutTarget = when (dataStatus) {
        is DataStatus.Loading -> 0
        is DataStatus.Error -> 1
        is DataStatus.Loaded, is DataStatus.Refreshing -> 2
    }

    Crossfade(
        targetState = layoutTarget,
        label = "StatefulContentContainerCrossfade",
        modifier = modifier
    ) { target ->
        when (target) {
            0 -> loadingContent()
            1 -> {
                val message = (dataStatus as? DataStatus.Error)?.message ?: UiText.DynamicString("Error")
                errorContent(message, onErrorDismiss)
            }
            2 -> {
                if (onRefresh != null) {
                    PullToRefreshBox(
                        isRefreshing = dataStatus is DataStatus.Refreshing,
                        onRefresh = onRefresh,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        content()
                    }
                } else {
                    content()
                }
            }
        }
    }
}
