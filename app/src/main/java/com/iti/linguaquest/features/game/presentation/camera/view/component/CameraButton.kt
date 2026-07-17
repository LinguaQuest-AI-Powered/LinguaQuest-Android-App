package com.iti.linguaquest.features.game.presentation.camera.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun CameraButton(
    onCaptureClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onCaptureClicked,
        modifier = modifier
            .size(80.dp)
            .background(MaterialTheme.colorScheme.primary, CircleShape)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LinguaQuestTheme.colors.whiteColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = stringResource(R.string.camera_alt),
                tint = LinguaQuestTheme.colors.blackColor,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}