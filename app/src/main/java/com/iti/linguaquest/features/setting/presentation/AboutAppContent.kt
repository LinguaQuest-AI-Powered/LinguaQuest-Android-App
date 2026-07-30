package com.iti.linguaquest.features.setting.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ShareTopBar
import com.iti.linguaquest.features.setting.presentation.components.AboutAppTopicCard

@Composable
fun AboutAppContent(
    onBackClick: () -> Unit,
    onFaqsClick: () -> Unit = {},
    onContactUsClick: () -> Unit = {},
    onUserGuideClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        ShareTopBar(
            title = R.string.settings_help_support,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.about_app_banner_title),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 38.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .width(44.dp)
                            .height(4.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(2.dp)
                            )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(id = R.string.about_app_banner_subtitle),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                        lineHeight = 22.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Image(
                    painter = painterResource(id = R.drawable.lingo_stting),
                    contentDescription = null,
                    modifier = Modifier.size(140.dp)
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = stringResource(id = R.string.about_app_section_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(18.dp))

            AboutAppTopicCard(
                title = stringResource(id = R.string.about_app_faqs_title),
                subtitle = stringResource(id = R.string.about_app_faqs_subtitle),
                icon = painterResource(id = R.drawable.lingo_hint),
                titleColor = LinguaQuestTheme.colors.aboutFaqTitle,
                containerBackgroundColor = LinguaQuestTheme.colors.aboutFaqContainerBg,
                borderColor = LinguaQuestTheme.colors.aboutFaqBorder,
                iconBoxBackgroundColor = LinguaQuestTheme.colors.aboutFaqIconBoxBg,
                buttonBackgroundColor = LinguaQuestTheme.colors.aboutFaqButtonBg,
                buttonIconTint = LinguaQuestTheme.colors.aboutFaqButtonIconTint,
                onClick = onFaqsClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            AboutAppTopicCard(
                title = stringResource(id = R.string.about_app_contact_title),
                subtitle = stringResource(id = R.string.about_app_contact_subtitle),
                icon = painterResource(id = R.drawable.lingo_mail),
                titleColor = LinguaQuestTheme.colors.aboutContactTitle,
                containerBackgroundColor = LinguaQuestTheme.colors.aboutContactContainerBg,
                borderColor = LinguaQuestTheme.colors.aboutContactBorder,
                iconBoxBackgroundColor = LinguaQuestTheme.colors.aboutContactIconBoxBg,
                buttonBackgroundColor = LinguaQuestTheme.colors.aboutContactButtonBg,
                buttonIconTint = LinguaQuestTheme.colors.aboutContactButtonIconTint,
                onClick = onContactUsClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            AboutAppTopicCard(
                title = stringResource(id = R.string.about_app_user_guide_title),
                subtitle = stringResource(id = R.string.about_app_user_guide_subtitle),
                icon = painterResource(id = R.drawable.lingo_writing),
                titleColor = LinguaQuestTheme.colors.aboutGuideTitle,
                containerBackgroundColor = LinguaQuestTheme.colors.aboutGuideContainerBg,
                borderColor = LinguaQuestTheme.colors.aboutGuideBorder,
                iconBoxBackgroundColor = LinguaQuestTheme.colors.aboutGuideIconBoxBg,
                buttonBackgroundColor = LinguaQuestTheme.colors.aboutGuideButtonBg,
                buttonIconTint = LinguaQuestTheme.colors.aboutGuideButtonIconTint,
                onClick = onUserGuideClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutAppContentLightPreview() {
    LinguaQuestTheme(darkTheme = false) {
        AboutAppContent(onBackClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun AboutAppContentDarkPreview() {
    LinguaQuestTheme(darkTheme = true) {
        AboutAppContent(onBackClick = {})
    }
}
