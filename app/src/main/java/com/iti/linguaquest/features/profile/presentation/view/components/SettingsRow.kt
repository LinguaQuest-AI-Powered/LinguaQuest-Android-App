package com.iti.linguaquest.features.profile.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.Card3DWrapper
import com.iti.linguaquest.core.theme.LinguaQuestTheme


@Composable
fun SettingsRow(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    ledgeHeight: Dp = 4.dp,
    cornerRadius: Dp = 12.dp
) {
    Card3DWrapper(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = LinguaQuestTheme.colors.ProfileCardColor,
        borderColor = LinguaQuestTheme.colors.ProfileCardBorderColor,
        onClick = onClick,
        ledgeHeight = ledgeHeight,
        cornerRadius = cornerRadius
    ) {
        val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(LinguaQuestTheme.colors.BrownText),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = LinguaQuestTheme.colors.whiteColor
                )
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = stringResource(R.string.settings_label),
                color = LinguaQuestTheme.colors.iconsColor,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = LinguaQuestTheme.colors.iconsColor,
                modifier = Modifier.scale(scaleX = if (isRtl) -1f else 1f, scaleY = 1f)
            )
        }
    }
}