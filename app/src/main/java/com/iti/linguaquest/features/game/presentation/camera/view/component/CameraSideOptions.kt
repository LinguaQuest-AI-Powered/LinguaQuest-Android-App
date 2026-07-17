package com.iti.linguaquest.features.game.presentation.camera.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun CameraSideOptions(
    isFlashEnabled: Boolean,
    onToggleFlash: () -> Unit,
    onFlipCamera: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(
            onClick = onToggleFlash,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = LinguaQuestTheme.colors.blackColor.copy(alpha = 0.5f),
                contentColor = LinguaQuestTheme.colors.whiteColor
            )
        ) {
            Icon(
                imageVector = if (isFlashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                contentDescription = stringResource(R.string.toggle_flash)
            )
        }

        IconButton(
            onClick = onFlipCamera,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = LinguaQuestTheme.colors.blackColor.copy(alpha = 0.5f),
                contentColor = LinguaQuestTheme.colors.whiteColor
            )
        ) {
            Icon(
                imageVector = Icons.Default.Cameraswitch,
                contentDescription = stringResource(R.string.flip_camera)
            )
        }
    }
}