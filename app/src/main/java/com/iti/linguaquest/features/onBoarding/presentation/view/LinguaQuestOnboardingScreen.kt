package com.iti.linguaquest.features.onBoarding.presentation.view

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun OnboardingScreen(
    onGetStartedClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val frames = listOf(
        R.drawable.lingo_onboarding_1,
        R.drawable.lingo_onboarding_6,
        R.drawable.lingo_onboarding_3,
        R.drawable.lingo_onboarding_7
    )

    var currentFrameIndex by remember { mutableIntStateOf(0) }
    var textVisible by remember { mutableStateOf(false) }
    var buttonsVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(150.milliseconds)
        textVisible = true
        delay(250.milliseconds)
        buttonsVisible = true
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000.milliseconds)
            currentFrameIndex = (currentFrameIndex + 1) % frames.size
        }
    }

    val textAlpha by animateFloatAsState(
        targetValue = if (textVisible) 1f else 0f,
        animationSpec = tween(700, easing = LinearEasing),
        label = "text_alpha"
    )
    val textOffsetY by animateFloatAsState(
        targetValue = if (textVisible) 0f else 60f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "text_offset"
    )

    val buttonsAlpha by animateFloatAsState(
        targetValue = if (buttonsVisible) 1f else 0f,
        animationSpec = tween(700, easing = LinearEasing),
        label = "buttons_alpha"
    )
    val buttonsOffsetY by animateFloatAsState(
        targetValue = if (buttonsVisible) 0f else 60f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "buttons_offset"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Crossfade(
            targetState = frames[currentFrameIndex],
            animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .aspectRatio(1f),
            label = "mascot_crossfade"
        ) { frameRes ->
            Image(
                painter = painterResource(id = frameRes),
                contentDescription = stringResource(R.string.cd_linguaquest_mascot),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.weight(0.14f))

        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.onboarding_title_p1))
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.tertiary)) {
                    append(" ")
                }
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.tertiary)) {
                    append(stringResource(R.string.onboarding_title_p2))
                }
            },
            style = AppTextStyles.LessonTitle.copy(
                textAlign = TextAlign.Center,
                lineHeight = 30.sp,
                fontSize = 24.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = textOffsetY
                    alpha = textAlpha
                }
        )

        Spacer(modifier = Modifier.weight(0.2f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = buttonsOffsetY
                    alpha = buttonsAlpha
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp)
            ) {
                AppButton3D(
                    text = stringResource(R.string.get_started),
                    onClick = onGetStartedClick,
                    variant = ButtonVariant.PRIMARY
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp)
            ) {
                AppButton3D(
                    text = stringResource(R.string.already_have_an_account),
                    onClick = onLoginClick,
                    variant = ButtonVariant.SECONDARY
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.16f))
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun OnboardingScreenPreview() {
    LinguaQuestTheme {
        OnboardingScreen()
    }
}