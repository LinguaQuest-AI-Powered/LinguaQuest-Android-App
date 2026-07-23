package com.iti.linguaquest.features.voicegame.presentation.view.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox

@Composable
fun VoiceResultHeader(
    isPassed: Boolean,
    advice: String
) {
    SpeechBubble(advice)
    Spacer(Modifier.height(8.dp))

    AppMascotGradientBox(
        imageRes = if (isPassed) R.drawable.lingo_success else R.drawable.lingo_error,
        mascotOverlapHeight = 80.dp,
        mascotSize = 190.dp
    ) {
        Text(
            if (isPassed) stringResource(R.string.voice_result_great_job)
            else stringResource(R.string.voice_result_not_quite),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
