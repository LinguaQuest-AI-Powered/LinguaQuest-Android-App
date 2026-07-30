package com.iti.linguaquest.features.setting.presentation.about_app.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors

@Composable
fun AboutFooterSection(
    modifier: Modifier = Modifier
) {
    val colors = LocalLinguaQuestColors.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.about_app_footer_made_with),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = colors.BrownText.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(id = R.string.about_app_footer_copyright),
            fontSize = 11.sp,
            color = colors.BrownText.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
    }
}
