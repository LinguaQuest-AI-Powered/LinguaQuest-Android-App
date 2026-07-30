package com.iti.linguaquest.features.setting.presentation.about_app.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors

@Composable
fun AboutActionCardsSection(
    onRateAppClick: () -> Unit,
    onInstagramClick: () -> Unit,
    onWebsiteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalLinguaQuestColors.current

    Column(modifier = modifier.fillMaxWidth()) {
        AboutActionCard(
            icon = painterResource(id = R.drawable.ic_star),
            iconBackgroundColor = colors.Amber.copy(alpha = 0.15f),
            iconTintColor = colors.Amber,
            title = stringResource(id = R.string.about_app_rate_app),
            onClick = onRateAppClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        AboutActionCard(
            icon = painterResource(id = R.drawable.ic_camera),
            iconBackgroundColor = colors.SuccessAccent.copy(alpha = 0.15f),
            iconTintColor = colors.SuccessAccent,
            title = stringResource(id = R.string.about_app_follow_instagram),
            onClick = onInstagramClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        AboutActionCard(
            icon = painterResource(id = R.drawable.ic_profile_world),
            iconBackgroundColor = colors.Amber.copy(alpha = 0.15f),
            iconTintColor = colors.Amber,
            title = stringResource(id = R.string.about_app_visit_website),
            onClick = onWebsiteClick
        )
    }
}
