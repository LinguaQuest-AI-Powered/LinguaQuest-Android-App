package com.iti.linguaquest.features.setting.presentation.about_app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors

@Composable
fun AboutHeaderSection(
    modifier: Modifier = Modifier
) {
    val colors = LocalLinguaQuestColors.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.lingo),
            contentDescription = stringResource(id = R.string.about_app_name),
            modifier = Modifier.size(130.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = R.string.about_app_name),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colors.titleAndCationsColor
        )
    }
}
