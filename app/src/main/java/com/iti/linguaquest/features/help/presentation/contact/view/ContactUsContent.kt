package com.iti.linguaquest.features.help.presentation.contact.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ShareTopBar
import com.iti.linguaquest.features.help.presentation.contact.contract.ContactUsIntent
import com.iti.linguaquest.features.help.presentation.contact.contract.ContactUsState
import com.iti.linguaquest.features.help.presentation.contact.view.components.ContactHeroSection
import com.iti.linguaquest.features.help.presentation.contact.view.components.ContactInfoCard

@Composable
fun ContactUsContent(
    state: ContactUsState,
    onIntent: (ContactUsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        ShareTopBar(
            title = R.string.help_contact_title,
            onBackClick = { onIntent(ContactUsIntent.OnBackClicked) }
        )

        ContactHeroSection()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.contact_intro_title),
            modifier = Modifier.padding(horizontal = 20.dp),
            color = LinguaQuestTheme.colors.BrownText,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.contact_intro_subtitle),
            modifier = Modifier.padding(horizontal = 20.dp),
            color = LinguaQuestTheme.colors.iconsColor,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(14.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ContactInfoCard(
                title = stringResource(R.string.contact_email_label),
                content = state.email,
                accentColor = LinguaQuestTheme.colors.LeaderboardBlue
            )
            state.sections.forEachIndexed { index, section ->
                ContactInfoCard(
                    title = stringResource(section.titleRes),
                    content = stringResource(section.descriptionRes),
                    accentColor = when (index) {
                        0 -> LinguaQuestTheme.colors.LeaderboardGold
                        1 -> LinguaQuestTheme.colors.LeaderboardBlue
                        else -> MaterialTheme.colorScheme.tertiary
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
