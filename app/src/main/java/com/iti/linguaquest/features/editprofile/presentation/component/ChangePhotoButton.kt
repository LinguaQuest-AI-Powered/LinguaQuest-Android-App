package com.iti.linguaquest.features.editprofile.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iti.linguaquest.core.theme.AppColors

@Composable
fun ChangePhotoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = AppColors.BrownText
        )
    }
}
