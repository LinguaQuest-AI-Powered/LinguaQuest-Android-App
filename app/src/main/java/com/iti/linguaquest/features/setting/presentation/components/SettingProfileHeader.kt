package com.iti.linguaquest.features.setting.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.core.utils.ImageWrapper

@Composable
fun SettingProfileHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageWrapper(
            model = R.drawable.lingo_stting,
            contentDescription = null,
            modifier = Modifier.size(130.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Explorer Alex",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = LocalLinguaQuestColors.current.BrownText
        )
        Text(
            text = stringResource(id = R.string.settings_customize_journey),
            fontSize = 14.sp,
            color = LocalLinguaQuestColors.current.BrownText.copy(alpha = 0.7f)
        )
    }
}
