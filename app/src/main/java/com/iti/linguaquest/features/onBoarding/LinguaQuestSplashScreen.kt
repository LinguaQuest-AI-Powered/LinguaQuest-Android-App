package com.iti.linguaquest.features.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
import com.iti.linguaquest.ui.theme.AppTextStyles
import com.iti.linguaquest.ui.theme.LinguaQuestTheme

@Composable
fun LinguaQuestSplashScreen(modifier: Modifier = Modifier) {
    val backgroundBrush = Brush.linearGradient(
        colors = listOf(
            LinguaQuestTheme.colors.splashTopLeftColor,
            LinguaQuestTheme.colors.splashBottomRightColor
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(0.55f))

            Image(
                painter = painterResource(id = R.drawable.linguaquest_logo),
                contentDescription = "LinguaQuest",
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .wrapContentHeight(),
                contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier.weight(0.01f))

            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.linguaquest_circle),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                Image(
                    painter = painterResource(id = R.drawable.lingo_splash_parrot),
                    contentDescription = "LinguaQuest mascot",
                    modifier = Modifier
                        .fillMaxSize(0.72f)
                        .align(BiasAlignment(horizontalBias = -0.12f, verticalBias = 0f)),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.weight(0.35f))

            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.background,
                style = AppTextStyles.AppTitle
            )
            Spacer(modifier = Modifier.weight(2.15f))
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 890)
@Composable
private fun LinguaQuestSplashScreenPreview() {
    LinguaQuestSplashScreen()
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
private fun LinguaQuestSplashScreenPreviewLarge() {
    LinguaQuestSplashScreen()
}
