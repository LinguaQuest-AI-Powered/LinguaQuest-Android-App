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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.sharedComponents.MessageBubble
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun OfflineStateView(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    retryLabel: String? = null,
    onRetry: (() -> Unit)? = null
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
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    val imageContentDescription = stringResource(R.string.no_internet_image_desc)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(500)) +
                    scaleIn(
                        initialScale = 0.85f,
                        animationSpec = tween(
                            500,
                            easing = FastOutSlowInEasing
                        )
                    )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MessageBubble(
                    title = resolvedTitle,
                    subtitle = resolvedSubtitle
                )

                Spacer(modifier = Modifier.height(28.dp))

                Image(
                    painter = painterResource(id = R.drawable.lingo_offline_mode),
                    contentDescription = imageContentDescription,
                    modifier = Modifier
                        .size(280.dp)
                        .offset(y = floatOffsetDp.dp),
                    contentScale = ContentScale.Fit
                )

                if (onRetry != null) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onRetry,
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(
                            Icons.Filled.Refresh,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(resolvedRetryLabel)
                    }
                }
            }
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
