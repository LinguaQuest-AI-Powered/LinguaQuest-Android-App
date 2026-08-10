package com.iti.linguaquest.core.sharedComponents

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    message: String? = null,
    imageRes: Int? = null,
    onDismissRequest: (() -> Unit)? = null
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }

    val currentImageRes = remember(imageRes) {
        imageRes ?: listOf(
            R.drawable.lingo_searching,
            R.drawable.lingo_mind_thinking,
            R.drawable.lingo_mind_processing,
            R.drawable.lingo_checking_pronounciation,
            R.drawable.lingo_help,
            R.drawable.lingo_did_you_know,
            R.drawable.lingo_hint,
            R.drawable.lingo_memory_track
        ).random()
    }

    val randomMessageRes = remember {
        listOf(
            R.string.loading_message_1,
            R.string.loading_message_2,
            R.string.loading_message_3,
            R.string.loading_message_4,
            R.string.loading_message_5
        ).random()
    }
    
    val currentMessage = message ?: stringResource(randomMessageRes)

    val infiniteTransition = rememberInfiniteTransition(label = "loading_pop")

    val translateY by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "loading_translateY"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "loading_scale"
    )

    Dialog(
        onDismissRequest = { onDismissRequest?.invoke() },
        properties = DialogProperties(
            dismissOnBackPress = onDismissRequest != null,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth(0.85f)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(400)) + 
                        scaleIn(initialScale = 0.85f, animationSpec = tween(400, easing = FastOutSlowInEasing))
            ) {
                AppGradientBackgroundBox(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(
                    LinguaQuestTheme.colors.DialogGradientTopRight,
                    LinguaQuestTheme.colors.whiteColor,
                    LinguaQuestTheme.colors.DialogGradientBottomLeft
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 16.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .background(LinguaQuestTheme.colors.MindReaderBeige)
                    ) {
                        Image(
                            painter = painterResource(id = currentImageRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(130.dp)
                                .graphicsLayer {
                                    translationY = translateY
                                    scaleX = scale
                                    scaleY = scale
                                }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = currentMessage,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = LinguaQuestTheme.colors.BrownText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    LingoBouncingDots(
                        dotColor = LinguaQuestTheme.colors.BrownText
                    )
                }
                }
            }
        }
    }
}
@Preview
@Composable
fun LoadingViewPreview() {
    LinguaQuestTheme {
        LoadingView(
            message = "Loading your quest..."
        )
    }
}


