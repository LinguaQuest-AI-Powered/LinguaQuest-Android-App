package com.iti.linguaquest.features.voicegame.presentation.view.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.ButtonVariant

@Composable
fun VoiceResultActionButtons(
    isPassed: Boolean,
    onContinue: () -> Unit,
    onRetry: () -> Unit,
    onHome: () -> Unit
) {
    if (isPassed) {
        AppButton3D(
            text = stringResource(R.string.voice_result_continue),
            onClick = onContinue,
            variant = ButtonVariant.PRIMARY
        )
        Spacer(Modifier.height(12.dp))
        AppButton3D(
            text = stringResource(R.string.voice_result_return_home),
            onClick = onHome,
            variant = ButtonVariant.SECONDARY
        )
    } else {
        AppButton3D(
            text = stringResource(R.string.voice_result_retry),
            icon = rememberVectorPainter(image = Icons.AutoMirrored.Filled.ArrowBack),
            onClick = onRetry,
            variant = ButtonVariant.PRIMARY
        )
        Spacer(Modifier.height(12.dp))
        AppButton3D(
            text = stringResource(R.string.voice_result_return_home),
            onClick = onHome,
            variant = ButtonVariant.SECONDARY
        )
    }
}
