package com.iti.linguaquest.features.onBoarding.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import kotlin.collections.forEach
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.iti.linguaquest.features.home.domain.model.LanguageOption

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
    var anchorWidthPx by remember { mutableStateOf(0) }
    val density = LocalDensity.current

    val arrowRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "arrowRotation"
    )

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
                border = BorderStroke(
                    1.5.dp,
                    accentColor.copy(alpha = if (isExpanded) 0.9f else 0.4f)
                ),
                color = accentColor.copy(alpha = 0.06f),
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { anchorWidthPx = it.size.width }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedText ?: placeholder,
                        color = if (selectedText != null) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.rotate(arrowRotation)
                    )
                }
            }

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = onToggle,
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                modifier = Modifier
                    .width(with(density) { anchorWidthPx.toDp() })
                    .heightIn(max = 260.dp)
            ) {
                options.forEach { option ->
                    val isSelected = option.name == selectedText
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option.name,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurface
                            )
                        },
                        leadingIcon = {
                            AsyncImage(
                                model = option.imageUrl,
                                contentDescription = null,
                                modifier = Modifier.size(22.dp).clip(CircleShape)
                            )
                        },
                        trailingIcon = {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = accentColor,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        onClick = { onSelect(option) },
                        modifier = Modifier.background(
                            if (isSelected) accentColor.copy(alpha = 0.08f) else Color.Transparent
                        )
                    )
                }
            }
        }
    }
}