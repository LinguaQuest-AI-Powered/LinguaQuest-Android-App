package com.iti.linguaquest.features.help.presentation.help.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBarBackButtonStyle
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.core.utils.ImageWrapper
import com.iti.linguaquest.features.help.presentation.help.contract.HelpIntent
import com.iti.linguaquest.features.help.presentation.help.contract.HelpState
import com.iti.linguaquest.features.help.presentation.help.view.components.FaqAccordionItem
import com.iti.linguaquest.features.help.presentation.help.view.components.HelpActionCard
import com.iti.linguaquest.features.help.presentation.help.view.components.HeroBubble

@Composable
fun HelpContent(
    state: HelpState,
    onIntent: (HelpIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalLinguaQuestColors.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 8.dp, bottom = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {

        LinguaQuestScreenTopBar(
            title = stringResource(id = R.string.help_support_title),
            onBackClicked = { onIntent(HelpIntent.OnBackClicked) },
            isTitleCentered = true,
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            titleColor = LocalLinguaQuestColors.current.BrownText,
            titleTextStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 20.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            ),
            showDivider = true,
            dividerSpacing = 16.dp,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 22.dp, vertical = 0.dp),
            backButtonStyle = LinguaQuestScreenTopBarBackButtonStyle.Circular,
            backButtonSize = 40.dp,
            backButtonBackgroundColor = LocalLinguaQuestColors.current.whiteColor,
            backButtonContentColor = LocalLinguaQuestColors.current.OrangeActive,
            backButtonIconSize = 18.dp
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            HeroBubble(
                text = stringResource(id = R.string.help_banner_speech_bubble),
                bubbleColor = colors.whiteColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            ImageWrapper(
                model = R.drawable.lingo_help,
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.help_faq_section_title),
                style = AppTextStyles.SectionTitle.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                state.faqItems.forEach { faq ->
                    FaqAccordionItem(
                        faq = faq,
                        isExpanded = faq.id == state.expandedFaqId,
                        onToggle = { onIntent(HelpIntent.OnFaqToggled(faq.id)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.help_still_need_help_title),
                style = AppTextStyles.SectionTitle.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            HelpActionCard(
                onContactSupportClick = { onIntent(HelpIntent.OnContactSupportClicked) },
                onReportBugClick = { onIntent(HelpIntent.OnReportBugClicked) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = R.string.help_reply_footer),
                style = AppTextStyles.Caption,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}
