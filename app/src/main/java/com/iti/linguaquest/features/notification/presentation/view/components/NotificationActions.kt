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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun NotificationActions(
    isUnread: Boolean,
    onDeleteClick: (Rect) -> Unit,
    modifier: Modifier = Modifier
) {
    val deleteInteractionSource = remember { MutableInteractionSource() }
    val isDeletePressed by deleteInteractionSource.collectIsPressedAsState()
    val deleteOffset by animateDpAsState(
        targetValue = if (isDeletePressed) 2.dp else 0.dp,
        animationSpec = tween(durationMillis = 80),
        label = "deleteOffset"
    )
    var deleteBounds by remember { mutableStateOf(Rect.Zero) }

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxHeight()
            .padding(vertical = 2.dp)
    ) {
        Box(modifier = Modifier.size(32.dp)) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(top = 2.dp)
                    .clip(CircleShape)
                    .background(LinguaQuestTheme.colors.textFieldBorder)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .offset(y = deleteOffset)
                    .clip(CircleShape)
                    .background(LinguaQuestTheme.colors.textFieldFill)
                    .onGloballyPositioned { coordinates ->
                        deleteBounds = coordinates.boundsInRoot()
                    }
                    .clickable(
                        interactionSource = deleteInteractionSource,
                        indication = null,
                        onClick = { onDeleteClick(deleteBounds) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = stringResource(R.string.delete_notification_title),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
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