package com.iti.linguaquest.features.setting.presentation.components
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
  fun SectionDivider(paddingHorizontal: Dp = 16.dp) {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.background,
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = paddingHorizontal)
    )
}