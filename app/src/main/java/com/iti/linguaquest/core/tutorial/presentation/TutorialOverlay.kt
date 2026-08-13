package com.iti.linguaquest.core.tutorial.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.tutorial.domain.TutorialManager

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

        val targetTopDp = targetRect?.let { with(density) { it.top.toDp() } }
        val cardAligned = if (targetTopDp != null && targetTopDp > 400.dp) {
            Alignment.TopCenter
        } else {
            Alignment.BottomCenter
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) { detectTapGestures { } }
            ) {
                with(drawContext.canvas.nativeCanvas) {
                    val checkpoint = saveLayer(null, null)
                    drawRect(color = Color.Black.copy(alpha = 0.75f), size = size)
                    if (targetRect != null) {
                        val pad = 8.dp.toPx()
                        drawRoundRect(
                            color = Color.Transparent,
                            topLeft = Offset(
                                (targetRect.left - pad).coerceAtLeast(0f),
                                (targetRect.top - pad).coerceAtLeast(0f)
                            ),
                            size = Size(
                                (targetRect.width + pad * 2).coerceAtMost(size.width),
                                (targetRect.height + pad * 2).coerceAtMost(size.height)
                            ),
                            cornerRadius = CornerRadius(16.dp.toPx()),
                            blendMode = BlendMode.Clear
                        )
                    }
                    restoreToCount(checkpoint)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(
                        top = if (cardAligned == Alignment.TopCenter) 80.dp else 0.dp,
                        bottom = if (cardAligned == Alignment.TopCenter) 0.dp else 120.dp
                    ),
                contentAlignment = cardAligned
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = LinguaQuestTheme.colors.ProfileCardColor,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = LinguaQuestTheme.colors.ProfileCardBorderColor,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Crossfade(
                        targetState = state.currentStepIndex,
                        label = "tutorial_step_content"
                    ) { stepIndex ->
                        val step = tour.steps.getOrNull(stepIndex) ?: return@Crossfade
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = stringResource(id = step.titleRes),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = LinguaQuestTheme.colors.BrownText,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = stringResource(id = R.string.skip),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = LinguaQuestTheme.colors.BrownText.copy(alpha = 0.6f),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable { manager.skipTour() }
                                        .padding(start = 16.dp, bottom = 8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = step.lingoImageRes),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .size(72.dp)
                                        .background(
                                            color = LinguaQuestTheme.colors.OrangeActive.copy(alpha = 0.12f),
                                            shape = CircleShape
                                        )
                                        .padding(8.dp)
                                )
                                Text(
                                    text = stringResource(id = step.descriptionRes),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = LinguaQuestTheme.colors.BrownText.copy(alpha = 0.8f),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

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
                                        .width(if (isActive) 20.dp else 8.dp)
                                        .height(8.dp)
                                        .background(
                                            color = if (isActive) LinguaQuestTheme.colors.OrangeActive
                                            else LinguaQuestTheme.colors.BrownText.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                )
                            }
                        }
                        val isLast = state.currentStepIndex == tour.steps.lastIndex
                        AppButton3D(
                            text = stringResource(id = if (isLast) R.string.got_it else R.string.next),
                            onClick = { manager.nextStep() },
                            buttonHeight = 44.dp,
                            modifier = Modifier.width(130.dp)
                        )
                    }
                }
            }
        }
    }
}
