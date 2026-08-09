package com.iti.linguaquest.features.setting.presentation.components
import com.iti.linguaquest.core.theme.LinguaQuestTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.core.sound.AppSound

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    icon: Painter,
    title: String,
    iconTint: Color = LocalLinguaQuestColors.current.iconsColor,
    value: String? = null,
    valueColor: Color = LocalLinguaQuestColors.current.BrownText,
    hasSwitch: Boolean = false,
    switchChecked: Boolean = false,
    onSwitchChange: ((Boolean) -> Unit)? = null,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
) {
    val soundPlayer = LocalSoundPlayer.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.45f)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = LocalLinguaQuestColors.current.titleAndCationsColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        if (value != null) {
            Text(
                text = value,
                color = valueColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        if (hasSwitch) {
            Switch(
                checked = switchChecked,
                enabled = enabled,
                onCheckedChange = { checked ->
                    soundPlayer.play(AppSound.SWITCH)
                    onSwitchChange?.invoke(checked)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = LinguaQuestTheme.colors.whiteColor,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = LinguaQuestTheme.colors.whiteColor,
                    uncheckedTrackColor = Color.LightGray,
                    uncheckedBorderColor = Color.Transparent
                ),
                modifier = Modifier.height(24.dp)
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_setting_arro),
                contentDescription = null,
                tint = LocalLinguaQuestColors.current.iconsColor.copy(alpha = 0.5f),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
