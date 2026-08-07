package com.iti.linguaquest.features.profile.presentation.view.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun SettingsRow(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    ledgeHeight: Dp = 4.dp,
    cornerRadius: Dp = 12.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val pressOffset by animateDpAsState(
        targetValue = if (isPressed) ledgeHeight else 0.dp,
        animationSpec = tween(durationMillis = 80),
        label = "settingsRowPressOffset"
    )

    val shape = RoundedCornerShape(cornerRadius)
    val cardColor = LinguaQuestTheme.colors.ProfileCardColor
    val borderColor = LinguaQuestTheme.colors.ProfileCardBorderColor

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = ledgeHeight)
    ) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = ledgeHeight)
                .clip(shape)
                .background(borderColor)
        )

        Surface(
            shape = shape,
            color = cardColor,
            border = BorderStroke(1.dp, borderColor),
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = pressOffset)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
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
}