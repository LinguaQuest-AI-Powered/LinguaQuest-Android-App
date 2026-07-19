package com.iti.linguaquest.core.sharedComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.AppTextStyles

@Composable
fun AppOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = AppColors.DialogSecondaryButtonOutline,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50),
        enabled = enabled,
        border = BorderStroke(2.dp, color),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = color
        ),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp)
    ) {
        Text(
            text = text,
            style = AppTextStyles.Button,
            color = color
        )
    }
}