package com.iti.linguaquest.core.sharedComponents.snackbar


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun AppSnackbarHost(hostState: SnackbarHostState, modifier: Modifier = Modifier) {
    SnackbarHost(hostState = hostState, modifier = modifier) { data ->
        val visuals = data.visuals as? AppSnackbarVisuals
        val type = visuals?.type ?: SnackbarType.INFO

        val (containerColor, contentColor, icon) = when (type) {
            SnackbarType.SUCCESS -> Triple(Color(0xFF2E7D32), LinguaQuestTheme.colors.whiteColor, Icons.Default.CheckCircle)
            SnackbarType.ERROR -> Triple(MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.onError, Icons.Default.Error)
            SnackbarType.WARNING -> Triple(MaterialTheme.colorScheme.primary, Color.Black, Icons.Default.Warning)
            SnackbarType.INFO -> Triple(MaterialTheme.colorScheme.inverseSurface, MaterialTheme.colorScheme.inverseOnSurface, Icons.Default.Info)
        }

        val actionLabel = data.visuals.actionLabel

        Snackbar(
            modifier = Modifier.padding(12.dp),
            shape = RoundedCornerShape(12.dp),
            containerColor = containerColor,
            contentColor = contentColor,
            actionContentColor = contentColor,
            action = if (actionLabel != null) {
                {
                    TextButton(onClick = { data.performAction() }) {
                        Text(actionLabel, color = contentColor, fontWeight = FontWeight.Bold)
                    }
                }
            } else null
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = contentColor)
                Spacer(Modifier.width(10.dp))
                Text(data.visuals.message)
            }
        }
    }
}