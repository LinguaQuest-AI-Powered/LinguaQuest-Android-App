package com.iti.linguaquest.core.sharedComponents.snackbar

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.theme.LinguaQuestTheme


private data class SnackbarStyle(
    val gradient: List<Color>,
    val outline: Color,
    val contentColor: Color,
    val iconBadgeBg: Color,
    val iconTint: Color,
    val shadowColor: Color,
    val defaultIcon: ImageVector
)

@Composable
private fun styleFor(type: SnackbarType): SnackbarStyle = when (type) {
    SnackbarType.SUCCESS -> SnackbarStyle(
        gradient = listOf(LinguaQuestTheme.colors.Linen, LinguaQuestTheme.colors.Sand),
        outline = LinguaQuestTheme.colors.SuccessAccent.copy(alpha = 0.5f),
        contentColor = LinguaQuestTheme.colors.Espresso,
        iconBadgeBg = LinguaQuestTheme.colors.SuccessAccent,
        iconTint = LinguaQuestTheme.colors.whiteColor,
        shadowColor = LinguaQuestTheme.colors.SuccessAccent,
        defaultIcon = Icons.Default.CheckCircle
    )
    SnackbarType.ERROR -> SnackbarStyle(
        gradient = listOf(LinguaQuestTheme.colors.Charcoal, LinguaQuestTheme.colors.Espresso),
        outline = LinguaQuestTheme.colors.ErrorAccent.copy(alpha = 0.6f),
        contentColor = LinguaQuestTheme.colors.Linen,
        iconBadgeBg = LinguaQuestTheme.colors.ErrorAccent,
        iconTint = LinguaQuestTheme.colors.whiteColor,
        shadowColor = LinguaQuestTheme.colors.ErrorAccent,
        defaultIcon = Icons.Default.Error
    )
    SnackbarType.WARNING -> SnackbarStyle(
        gradient = listOf(LinguaQuestTheme.colors.Amber, Color(0xFFE08A1E)),
        outline = LinguaQuestTheme.colors.Espresso.copy(alpha = 0.35f),
        contentColor = LinguaQuestTheme.colors.Charcoal,
        iconBadgeBg = LinguaQuestTheme.colors.Espresso,
        iconTint = LinguaQuestTheme.colors.Amber,
        shadowColor = LinguaQuestTheme.colors.Amber,
        defaultIcon = Icons.Default.Warning
    )
    SnackbarType.INFO -> SnackbarStyle(
        gradient = listOf(LinguaQuestTheme.colors.Charcoal, Color(0xFF34302B)),
        outline = LinguaQuestTheme.colors.InfoAccent.copy(alpha = 0.5f),
        contentColor = LinguaQuestTheme.colors.Linen,
        iconBadgeBg = LinguaQuestTheme.colors.InfoAccent,
        iconTint = LinguaQuestTheme.colors.whiteColor,
        shadowColor = LinguaQuestTheme.colors.InfoAccent,
        defaultIcon = Icons.Default.Info
    )
}

private fun SnackbarDuration.approxMillis(): Long? = when (this) {
    SnackbarDuration.Short -> 4000L
    SnackbarDuration.Long -> 7000L
    SnackbarDuration.Indefinite -> null
}

@Composable
fun AppSnackbarHost(hostState: SnackbarHostState, modifier: Modifier = Modifier) {
    SnackbarHost(hostState = hostState, modifier = modifier) { data ->
        AppSnackbar(data)
    }
}

@Composable
private fun AppSnackbar(data: SnackbarData) {
    val visuals = data.visuals as? AppSnackbarVisuals
    val type = visuals?.type ?: SnackbarType.INFO
    val style = styleFor(type)
    val icon = visuals?.icon ?: style.defaultIcon
    val actionLabel = data.visuals.actionLabel
    val title = visuals?.title
    val showClose = visuals?.showCloseIcon ?: false

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .shadow(14.dp, RoundedCornerShape(20.dp), spotColor = style.shadowColor.copy(alpha = 0.45f))
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = style.gradient,
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = if (showClose) 8.dp else 16.dp, top = 14.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(style.iconBadgeBg, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = style.iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    if (title != null) {
                        Text(
                            text = title,
                            color = style.contentColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(2.dp))
                    }
                    Text(
                        text = data.visuals.message,
                        color = style.contentColor.copy(alpha = if (title != null) 0.85f else 1f),
                        fontWeight = if (title != null) FontWeight.Normal else FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }

                if (actionLabel != null) {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = actionLabel,
                        color = style.iconBadgeBg,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(style.contentColor.copy(alpha = 0.1f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .then(
                                Modifier.clickableAction { data.performAction() }
                            )
                    )
                }

                if (showClose) {
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(style.contentColor.copy(alpha = 0.12f))
                            .clickableAction { data.dismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss",
                            tint = style.contentColor,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            val totalMillis = data.visuals.duration.approxMillis()
            if (totalMillis != null) {
                CountdownBar(totalMillis = totalMillis, color = style.iconBadgeBg)
            }
        }
    }
}

@Composable
private fun CountdownBar(totalMillis: Long, color: Color) {

    var target by remember(totalMillis) { mutableFloatStateOf(1f) }
    LaunchedEffect(totalMillis) {
        target = 0f
    }
    val animatedProgress by animateFloatAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = totalMillis.toInt(), easing = LinearEasing),
        label = "snackbar_countdown"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
            .background(color.copy(alpha = 0.15f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = animatedProgress.coerceIn(0f, 1f))
                .height(3.dp)
                .background(color)
        )
    }
}

@Composable
private fun Modifier.clickableAction(onClick: () -> Unit): Modifier =
    this.then(
        Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = onClick
        )
    )