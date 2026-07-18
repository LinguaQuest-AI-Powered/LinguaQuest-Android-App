package com.iti.linguaquest.features.all_worlds.presentation.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun AllWorldsHeader(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.all_worlds_title),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = LinguaQuestTheme.colors.blackColor,
                fontSize = 32.sp
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.all_worlds_subtitle),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = LinguaQuestTheme.colors.iconsColor
            )
        )
    }
}
