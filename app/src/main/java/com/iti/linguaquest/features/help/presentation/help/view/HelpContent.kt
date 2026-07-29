package com.iti.linguaquest.features.help.presentation.help.view

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
import com.iti.linguaquest.features.help.data.HelpTopic
import com.iti.linguaquest.features.help.presentation.help.contract.HelpIntent
import com.iti.linguaquest.features.help.presentation.help.contract.HelpState
import com.iti.linguaquest.features.help.presentation.help.view.components.HelpHeroSection
import com.iti.linguaquest.features.help.presentation.help.view.components.HelpTopicCard

@Composable
fun HelpContent(
    state: HelpState,
    onIntent: (HelpIntent) -> Unit,
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
            title = R.string.help_support_title,
            onBackClick = { onIntent(HelpIntent.OnBackClicked) }
        )

        HelpHeroSection()

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(R.string.help_how_can_we_help),
            modifier = Modifier.padding(horizontal = 20.dp),
            color = LinguaQuestTheme.colors.BrownText,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            HelpTopicCard(
                topic = HelpTopic.FAQS,
                isSelected = state.selectedTopic == HelpTopic.FAQS,
                onClick = { onIntent(HelpIntent.OnTopicSelected(HelpTopic.FAQS)) }
            )
            HelpTopicCard(
                topic = HelpTopic.CONTACT_US,
                isSelected = state.selectedTopic == HelpTopic.CONTACT_US,
                onClick = { onIntent(HelpIntent.OnTopicSelected(HelpTopic.CONTACT_US)) }
            )
            HelpTopicCard(
                topic = HelpTopic.USER_GUIDE,
                isSelected = state.selectedTopic == HelpTopic.USER_GUIDE,
                onClick = { onIntent(HelpIntent.OnTopicSelected(HelpTopic.USER_GUIDE)) }
            )
        }
    }
}
