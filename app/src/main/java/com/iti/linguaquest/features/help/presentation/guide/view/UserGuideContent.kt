package com.iti.linguaquest.features.help.presentation.guide.view

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
import com.iti.linguaquest.features.help.presentation.guide.contract.UserGuideIntent
import com.iti.linguaquest.features.help.presentation.guide.contract.UserGuideState
import com.iti.linguaquest.features.help.presentation.guide.view.components.UserGuideHeroSection
import com.iti.linguaquest.features.help.presentation.guide.view.components.UserGuideSectionCard

@Composable
fun UserGuideContent(
    state: UserGuideState,
    onIntent: (UserGuideIntent) -> Unit,
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
            title = R.string.help_guide_title,
            onBackClick = { onIntent(UserGuideIntent.OnBackClicked) }
        )

        UserGuideHeroSection()

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(R.string.user_guide_overview_label),
            modifier = Modifier.padding(horizontal = 20.dp),
            color = LinguaQuestTheme.colors.BrownText,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            state.sections.forEachIndexed { index, section ->
                UserGuideSectionCard(
                    emoji = section.emoji,
                    title = stringResource(section.titleRes),
                    description = stringResource(section.descriptionRes),
                    accentColor = if (index % 2 == 0) {
                        LinguaQuestTheme.colors.LeaderboardGold
                    } else {
                        LinguaQuestTheme.colors.LeaderboardBlue
                    },
                    titleColor = LinguaQuestTheme.colors.BrownText
                )
            }

            UserGuideSectionCard(
                emoji = "\u2728",
                title = stringResource(R.string.user_guide_product_vision_label),
                description = stringResource(R.string.user_guide_product_vision_desc),
                accentColor = MaterialTheme.colorScheme.tertiary,
                titleColor = MaterialTheme.colorScheme.onSurface,
                highlighted = true
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
