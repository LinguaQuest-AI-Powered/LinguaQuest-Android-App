package com.iti.linguaquest.core.tutorial.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.tutorial.domain.TutorialManager
import com.iti.linguaquest.core.tutorial.domain.model.TutorialIntent
import com.iti.linguaquest.core.tutorial.domain.model.TutorialState

@Composable
fun TutorialOverlayScreen(
    modifier: Modifier = Modifier,
    manager: TutorialManager? = LocalTutorialManager.current
) {
    if (manager == null) return
    val state by manager.state.collectAsState()
    TutorialOverlayContent(
        state = state,
        onNext = { manager.onIntent(TutorialIntent.NextStep) },
        onSkip = { manager.onIntent(TutorialIntent.SkipTour) },
        modifier = modifier
    )
}

@Composable
fun TutorialOverlayContent(
    state: TutorialState,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = state.isVisible && state.activeTour != null,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier.fillMaxSize()
    ) {
        val tour = state.activeTour ?: return@AnimatedVisibility
        val currentStep = tour.steps.getOrNull(state.currentStepIndex) ?: return@AnimatedVisibility
        val targetBounds = state.targets[currentStep.stepId]

        val configuration = LocalConfiguration.current

        var cardHeightDp by remember { mutableStateOf(0.dp) }

        val targetTopDp = targetBounds?.let { with(density) { it.top.toDp() } }
        val targetBottomDp = targetBounds?.let { with(density) { it.bottom.toDp() } }

        val targetY = if (targetTopDp != null) {
            if (targetTopDp > 400.dp) {
                (targetTopDp - cardHeightDp - 16.dp).coerceAtLeast(16.dp)
            } else {
                (targetBottomDp ?: 0.dp) + 16.dp
            }
        } else {
            120.dp
        }

        val animatedY by animateDpAsState(
            targetValue = targetY,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            label = "tutorial_card_y"
        )

        val targetLeft = targetBounds?.left ?: 0f
        val targetTop = targetBounds?.top ?: 0f
        val targetWidth = targetBounds?.width ?: 0f
        val targetHeight = targetBounds?.height ?: 0f

        val animLeft by animateFloatAsState(
            targetValue = targetLeft,
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
            label = "spotlight_left"
        )
        val animTop by animateFloatAsState(
            targetValue = targetTop,
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
            label = "spotlight_top"
        )
        val animWidth by animateFloatAsState(
            targetValue = targetWidth,
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
            label = "spotlight_width"
        )
        val animHeight by animateFloatAsState(
            targetValue = targetHeight,
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
            label = "spotlight_height"
        )

        val overlayColor = LinguaQuestTheme.colors.blackColor

        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) { detectTapGestures { } }
            ) {
                with(drawContext.canvas.nativeCanvas) {
                    val checkpoint = saveLayer(null, null)
                    drawRect(color = overlayColor.copy(alpha = 0.75f), size = size)
                    if (animWidth > 0f && animHeight > 0f) {
                        val pad = 8.dp.toPx()
                        drawRoundRect(
                            color = Color.Transparent,
                            topLeft = Offset(
                                (animLeft - pad).coerceAtLeast(0f),
                                (animTop - pad).coerceAtLeast(0f)
                            ),
                            size = Size(
                                (animWidth + pad * 2).coerceAtMost(size.width),
                                (animHeight + pad * 2).coerceAtMost(size.height)
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
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer { translationY = animatedY.toPx() }
                        .onGloballyPositioned { coordinates ->
                            val height = with(density) { coordinates.size.height.toDp() }
                            if (cardHeightDp != height) {
                                cardHeightDp = height
                            }
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
                                        .clickable { onSkip() }
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
                                            color = MaterialTheme.colorScheme.primary,
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
                                TutorialStepDot(isActive = index == state.currentStepIndex)
                            }
                        }
                        val isLast = state.currentStepIndex == tour.steps.lastIndex
                        AppButton3D(
                            text = stringResource(id = if (isLast) R.string.got_it else R.string.next),
                            onClick = { onNext() },
                            buttonHeight = 44.dp,
                            modifier = Modifier.width(130.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TutorialStepDot(
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val dotWidth by animateDpAsState(
        targetValue = if (isActive) 20.dp else 8.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "dot_width"
    )
    val dotColor by animateColorAsState(
        targetValue = if (isActive) LinguaQuestTheme.colors.OrangeActive
        else LinguaQuestTheme.colors.BrownText.copy(alpha = 0.3f),
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "dot_color"
    )
    Box(
        modifier = modifier
            .width(dotWidth)
            .height(8.dp)
            .background(
                color = dotColor,
                shape = RoundedCornerShape(4.dp)
            )
    )
}
