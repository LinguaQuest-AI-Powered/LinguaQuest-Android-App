package com.iti.linguaquest.core.sharedComponents
import com.iti.linguaquest.core.theme.AppColors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors

@Composable
fun LinguaQuestScreenTopBar(
    title: String,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isTitleCentered: Boolean = true,
    containerColor: Color = Color.Transparent,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    showDivider: Boolean = false,
    trailingContent: @Composable RowScope.() -> Unit = { 
        if (isTitleCentered) Spacer(modifier = Modifier.size(40.dp)) 
    }
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(containerColor)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(LocalLinguaQuestColors.current.whiteColor)
                    .clickable { onBackClicked() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Back",
                    tint = LocalLinguaQuestColors.current.OrangeActive,
                    modifier = Modifier.size(18.dp)
                )
            }

            if (!isTitleCentered) {
                Spacer(modifier = Modifier.width(16.dp))
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = titleColor,
                    fontSize = 20.sp
                ),
                modifier = Modifier
                    .weight(1f)
                    .then(if (isTitleCentered) Modifier.padding(horizontal = 8.dp) else Modifier),
                textAlign = if (isTitleCentered) TextAlign.Center else TextAlign.Start
            )

            trailingContent()
        }

        if (showDivider) {
            HorizontalDivider(
                color = LinguaQuestTheme.colors.ProfileCardBorderColor,
                thickness = 1.dp
            )
        }
    }
}
