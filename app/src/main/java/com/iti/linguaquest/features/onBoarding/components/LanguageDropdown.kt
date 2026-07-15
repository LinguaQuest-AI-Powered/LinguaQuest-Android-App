package com.iti.linguaquest.features.onBoarding.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import kotlin.collections.forEach
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.iti.linguaquest.features.onBoarding.contract.LanguageOption


@Composable
 fun LanguageDropdown(
    label: String,
    selectedText: String?,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    options: List<LanguageOption>,
    onSelect: (LanguageOption) -> Unit,
    accentColor: Color,
    placeholder: String = ""
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = accentColor,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(4.dp))
        Box {
            Surface(
                onClick = onToggle,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.5.dp, accentColor.copy(alpha = 0.4f)),
                color = accentColor.copy(alpha = 0.06f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedText ?: placeholder,
                        color = if (selectedText != null) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                }
            }
            DropdownMenu(expanded = isExpanded, onDismissRequest = onToggle) {
                options.forEach { option ->
                    DropdownMenuItem(text = { Text(option.displayName) }, onClick = { onSelect(option) })
                }
            }
        }
    }
}