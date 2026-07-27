package com.iti.linguaquest.core.sharedComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    message: String? = null,
    imageRes: Int = R.drawable.lingo_searching
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AppMascotGradientBox(
            imageRes = imageRes
        ) {
            LingoLoadingAnimation(
                size = 90.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = message ?: stringResource(R.string.loading),
                style = AppTextStyles.DialogMessage,
                color = LinguaQuestTheme.colors.BrownText,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
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