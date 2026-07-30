package com.iti.linguaquest.features.setting.presentation.about_app.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.features.setting.presentation.components.SectionDivider

@Composable
fun AboutGroupedLinksCard(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onLicensesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalLinguaQuestColors.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = colors.whiteColor
    ) {
        Column {
            AboutGroupedListItem(
                icon = painterResource(id = R.drawable.ic_help_icon),
                title = stringResource(id = R.string.about_app_terms_service),
                onClick = onTermsClick
            )

            SectionDivider()

            AboutGroupedListItem(
                icon = painterResource(id = R.drawable.ic_lock_icon),
                title = stringResource(id = R.string.about_app_privacy_policy),
                onClick = onPrivacyClick
            )

            SectionDivider()

            AboutGroupedListItem(
                icon = painterResource(id = R.drawable.ic_info_icon),
                title = stringResource(id = R.string.about_app_licenses_credits),
                onClick = onLicensesClick
            )
        }
    }
}
