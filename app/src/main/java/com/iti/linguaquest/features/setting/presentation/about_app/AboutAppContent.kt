package com.iti.linguaquest.features.setting.presentation.about_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.features.setting.presentation.about_app.components.AboutActionCardsSection
import com.iti.linguaquest.features.setting.presentation.about_app.components.AboutDescriptionCard
import com.iti.linguaquest.features.setting.presentation.about_app.components.AboutFooterSection
import com.iti.linguaquest.features.setting.presentation.about_app.components.AboutGroupedLinksCard
import com.iti.linguaquest.features.setting.presentation.about_app.components.AboutHeaderSection

@Composable
fun AboutAppContent(
    onBackClick: () -> Unit,
    onRateAppClick: () -> Unit,
    onInstagramClick: () -> Unit,
    onWebsiteClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onLicensesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 8.dp, bottom = 24.dp)
    ) {
        LinguaQuestScreenTopBar(
            title = stringResource(id = R.string.about_app_title),
            onBackClicked = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AboutHeaderSection()

            Spacer(modifier = Modifier.height(16.dp))

            AboutDescriptionCard()

            Spacer(modifier = Modifier.height(24.dp))

            AboutActionCardsSection(
                onRateAppClick = onRateAppClick,
                onInstagramClick = onInstagramClick,
                onWebsiteClick = onWebsiteClick
            )

            Spacer(modifier = Modifier.height(20.dp))

            AboutGroupedLinksCard(
                onTermsClick = onTermsClick,
                onPrivacyClick = onPrivacyClick,
                onLicensesClick = onLicensesClick
            )

            Spacer(modifier = Modifier.height(32.dp))

            AboutFooterSection()

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutAppContentPreview() {
    LinguaQuestTheme {
        AboutAppContent(
            onBackClick = {},
            onRateAppClick = {},
            onInstagramClick = {},
            onWebsiteClick = {},
            onTermsClick = {},
            onPrivacyClick = {},
            onLicensesClick = {}
        )
    }
}
