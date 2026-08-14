package com.iti.linguaquest.features.notification.presentation.view.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun NotificationActions(
    isUnread: Boolean = false,
    isDeleting: Boolean,
    onDeleteClick: (Rect) -> Unit,
    modifier: Modifier = Modifier
) {
    val deleteBounds = remember { arrayOf(Rect.Zero) }
    val deleteInteractionSource = remember { MutableInteractionSource() }
    val isDeletePressed by deleteInteractionSource.collectIsPressedAsState()
    val deleteOffset by animateDpAsState(
        targetValue = if (isDeletePressed) 0.dp else (-2).dp,
        label = "NotificationActionDeleteOffset"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(top = 2.dp)
                    .clip(CircleShape)
                    .background(LinguaQuestTheme.colors.textFieldBorder)
            )
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .graphicsLayer { translationY = deleteOffset.toPx() }
                    .clip(CircleShape)
                    .background(LinguaQuestTheme.colors.textFieldFill)
                    .onGloballyPositioned { coordinates ->
                        deleteBounds[0] = coordinates.boundsInRoot()
                    }
                    .clickable(
                        enabled = !isDeleting,
                        interactionSource = deleteInteractionSource,
                        indication = null,
                        onClick = { onDeleteClick(deleteBounds[0]) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isDeleting) {
                    LingoSpinningIcon(size = 16.dp)
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(R.string.delete_notification_title),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        if (isUnread) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}