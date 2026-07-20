package com.iti.linguaquest.core.utils

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors

@Composable
fun ShareTopBar(
    @StringRes title: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingContent: @Composable RowScope.() -> Unit = { Spacer(modifier = Modifier.size(40.dp)) }
) {
    ShareTopBar(
        titleText = stringResource(id = title),
        onBackClick = onBackClick,
        modifier = modifier,
        trailingContent = trailingContent
    )
}

@Composable
fun ShareTopBar(
    titleText: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingContent: @Composable RowScope.() -> Unit = { Spacer(modifier = Modifier.size(40.dp)) }
) {
    Column(modifier = modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(LocalLinguaQuestColors.current.whiteColor)
                    .clickable(onClick = onBackClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = stringResource(id = R.string.back),
                    tint = LocalLinguaQuestColors.current.OrangeActive,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = titleText,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = LocalLinguaQuestColors.current.BrownText
            )
            trailingContent()
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider(
            color = LinguaQuestTheme.colors.ProfileCardBorderColor,
            thickness = 1.dp
        )
    }
}