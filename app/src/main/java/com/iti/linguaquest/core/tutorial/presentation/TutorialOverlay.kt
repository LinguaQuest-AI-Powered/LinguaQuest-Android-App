package com.iti.linguaquest.core.tutorial.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.AppOutlinedButton
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.tutorial.domain.TutorialManager
import kotlin.math.roundToInt

@Composable
fun TutorialOverlay(
    manager: TutorialManager,
    modifier: Modifier = Modifier
) {
    val state by manager.state.collectAsState()
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = state.isVisible && state.activeTour != null,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier.fillMaxSize()
    ) {
        val tour = state.activeTour ?: return@AnimatedVisibility
        val currentStep = tour.steps.getOrNull(state.currentStepIndex) ?: return@AnimatedVisibility
        val targetRect = state.targets[currentStep.stepId]

        var overlayHeight by remember { mutableIntStateOf(0) }
        var overlayWidth by remember { mutableIntStateOf(0) }
        var tooltipHeight by remember { mutableIntStateOf(0) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    overlayHeight = coordinates.size.height
                    overlayWidth = coordinates.size.width
                }
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { }
                    }
            ) {
                with(drawContext.canvas.nativeCanvas) {
                    val checkpoint = saveLayer(null, null)
                    drawRect(
                        color = Color.Black.copy(alpha = 0.75f),
                        size = size
                    )
                    if (targetRect != null) {
                        val padding = 8.dp.toPx()
                        drawRoundRect(
                            color = Color.Transparent,
                            topLeft = Offset(
                                (targetRect.left - padding).coerceAtLeast(0f),
                                (targetRect.top - padding).coerceAtLeast(0f)
                            ),
                            size = Size(
                                (targetRect.width + padding * 2).coerceAtMost(size.width),
                                (targetRect.height + padding * 2).coerceAtMost(size.height)
                            ),
                            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
                            blendMode = BlendMode.Clear
                        )
                    }
                    restoreToCount(checkpoint)
                }
            }

            val yOffset = remember(targetRect, overlayHeight, tooltipHeight) {
                if (targetRect == null || overlayHeight == 0) {
                    (overlayHeight - tooltipHeight) / 2
                } else {
                    val targetCenterY = targetRect.top + targetRect.height / 2
                    val paddingPx = with(density) { 16.dp.toPx() }
                    if (targetCenterY > overlayHeight / 2) {
                        (targetRect.top - tooltipHeight - paddingPx).roundToInt()
                            .coerceAtLeast(paddingPx.roundToInt())
                    } else {
                        (targetRect.bottom + paddingPx).roundToInt()
                            .coerceAtMost((overlayHeight - tooltipHeight - paddingPx).roundToInt())
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        translationY = yOffset.toFloat()
                    }
                    .padding(horizontal = 24.dp)
                    .onGloballyPositioned { coordinates ->
                        tooltipHeight = coordinates.size.height
                    }
                    .background(
                        color = LinguaQuestTheme.colors.ProfileCardColor,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = LinguaQuestTheme.colors.ProfileCardBorderColor,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(id = currentStep.titleRes),
                        style = MaterialTheme.typography.titleLarge,
                        color = LinguaQuestTheme.colors.BrownText,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = currentStep.descriptionRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = LinguaQuestTheme.colors.BrownText.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            tour.steps.forEachIndexed { index, _ ->
                                val isActive = index == state.currentStepIndex
                                Box(
                                    modifier = Modifier
                                        .width(if (isActive) 16.dp else 8.dp)
                                        .height(8.dp)
                                        .background(
                                            color = if (isActive) {
                                                LinguaQuestTheme.colors.OrangeActive
                                            } else {
                                                LinguaQuestTheme.colors.BrownText.copy(alpha = 0.3f)
                                            },
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                )
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AppOutlinedButton(
                                text = stringResource(id = R.string.skip),
                                onClick = { manager.skipTour() },
                                buttonHeight = 36.dp
                            )
                            val isLast = state.currentStepIndex == tour.steps.lastIndex
                            AppButton3D(
                                text = stringResource(id = if (isLast) R.string.got_it else R.string.next),
                                onClick = { manager.nextStep() },
                                buttonHeight = 36.dp,
                                modifier = Modifier.width(90.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
