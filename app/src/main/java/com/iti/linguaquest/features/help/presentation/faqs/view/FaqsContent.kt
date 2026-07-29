package com.iti.linguaquest.features.help.presentation.faqs.view

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
import com.iti.linguaquest.features.help.presentation.faqs.contract.FaqsIntent
import com.iti.linguaquest.features.help.presentation.faqs.contract.FaqsState
import com.iti.linguaquest.features.help.presentation.faqs.view.components.FaqHeroSection
import com.iti.linguaquest.features.help.presentation.faqs.view.components.FaqConversationCard

@Composable
fun FaqsContent(
    state: FaqsState,
    onIntent: (FaqsIntent) -> Unit,
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
            title = R.string.help_faqs_title,
            onBackClick = { onIntent(FaqsIntent.OnBackClicked) }
        )

        FaqHeroSection()

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(R.string.faqs_intro_title),
            modifier = Modifier.padding(horizontal = 20.dp),
            color = LinguaQuestTheme.colors.BrownText,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.faqs_intro_subtitle),
            modifier = Modifier.padding(horizontal = 20.dp),
            color = LinguaQuestTheme.colors.iconsColor,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(14.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            state.items.forEachIndexed { index, item ->
                FaqConversationCard(
                    questionText = stringResource(item.questionRes),
                    answerText = stringResource(item.answerRes),
                    questionImageRes = item.questionImageRes,
                    answerImageRes = item.answerImageRes,
                    accentColor = when (index % 3) {
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
