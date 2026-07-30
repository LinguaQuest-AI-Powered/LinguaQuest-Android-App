package com.iti.linguaquest.features.setting.presentation.about_app.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors

@Composable
fun AboutDescriptionCard(
    modifier: Modifier = Modifier
) {
    val colors = LocalLinguaQuestColors.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = colors.whiteColor
    ) {
        Text(
            text = stringResource(id = R.string.about_app_description),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = colors.BrownText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)
        )
    }
}
