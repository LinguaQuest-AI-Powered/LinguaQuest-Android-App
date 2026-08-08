package com.iti.linguaquest.core.sharedComponents.offline

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.sharedComponents.MessageBubble
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun OfflineStateView(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    retryLabel: String? = null,
    onRetry: (() -> Unit)? = null,
    onDismissRequest: (() -> Unit)? = null
) {
    val resolvedTitle = title ?: stringResource(R.string.no_internet_title)
    val resolvedSubtitle = subtitle ?: stringResource(R.string.no_internet_subtitle)
    val resolvedRetryLabel = retryLabel ?: stringResource(R.string.retry_label)

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(120.milliseconds)
        visible = true
    }

    val infiniteTransition = rememberInfiniteTransition(label = "offline_anim")
    val floatOffsetDp by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    val badgePulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(400)) +
                    scaleIn(initialScale = 0.9f, animationSpec = tween(400, easing = FastOutSlowInEasing))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MessageBubble(
                    title = resolvedTitle,
                    subtitle = resolvedSubtitle
                )

                Spacer(modifier = Modifier.height(16.dp))

                OfflineMascotSection(
                    floatOffsetDp = floatOffsetDp,
                    badgePulseAlpha = badgePulseAlpha,
                    retryLabel = resolvedRetryLabel,
                    onRetry = onRetry
                )
            }
        }
    }
}

@Composable
private fun OfflineMascotSection(
    floatOffsetDp: Float,
    badgePulseAlpha: Float,
    retryLabel: String,
    onRetry: (() -> Unit)?
) {
    var isRetrying by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.offset(y = floatOffsetDp.dp)
    ) {
        AppMascotGradientBox(
            imageRes = R.drawable.lingo_offline_mode,
            mascotSize = 220.dp,
            mascotOverlapHeight = 50.dp
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            if (onRetry != null) {
                AppButton3D(
                    text = retryLabel,
                    onClick = {
                        if (!isRetrying) {
                            isRetrying = true
                            onRetry()
                            scope.launch {
                                delay(1200.milliseconds)
                                isRetrying = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.85f),
                    variant = ButtonVariant.PRIMARY,
                    isLoading = isRetrying
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-16).dp, y = 30.dp)
                .shadow(6.dp, CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.error,
                    shape = CircleShape
                )
                .border(
                    width = 2.dp,
                    color = LinguaQuestTheme.colors.whiteColor,
                    shape = CircleShape
                )
                .padding(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.WifiOff,
                contentDescription = null,
                tint = LinguaQuestTheme.colors.whiteColor.copy(alpha = badgePulseAlpha),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
fun OfflineAwareContent(
    isOnline: Boolean,
    modifier: Modifier = Modifier,
    topBarTitle: String? = null,
    onBackClicked: (() -> Unit)? = null,
    showCoins: Boolean = false,
    coinsCount: Int = 0,
    showXp: Boolean = false,
    xpCount: Int = 0,
    onRetry: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Crossfade(
        targetState = isOnline,
        animationSpec = tween(400),
        label = "offline_crossfade",
        modifier = modifier
    ) { online ->
        if (online) {
            content()
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                if (onBackClicked != null || topBarTitle != null) {
                    LinguaQuestScreenTopBar(
                        title = topBarTitle,
                        onBackClicked = { onBackClicked?.invoke() },
                        showCoins = showCoins,
                        coinsCount = coinsCount,
                        showXp = showXp,
                        xpCount = xpCount,
                        showDivider = true
                    )
                }
                OfflineStateView(
                    modifier = Modifier.weight(1f),
                    onRetry = onRetry
                )
            }
        }
    }
}


