package com.iti.linguaquest.features.setting.presentation.about_app

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.core.utils.ShareTopBar
import com.iti.linguaquest.features.setting.presentation.components.SectionDivider

@Composable
fun AboutAppContent(
    onBackClick: () -> Unit,
    onRateAppClick: () -> Unit,
    onWebsiteClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onTermsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalLinguaQuestColors.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        ShareTopBar(
            title = R.string.about_app_title,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    colors.Amber.copy(alpha = 0.35f),
                                    colors.OrangeActive.copy(alpha = 0.15f),
                                    Color.Transparent
                                )
                            )
                        )
                )
                Image(
                    painter = painterResource(id = R.drawable.lingo_stting),
                    contentDescription = stringResource(id = R.string.about_app_name),
                    modifier = Modifier.size(130.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.about_app_name),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colors.titleAndCationsColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = colors.OrangeActive.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, colors.OrangeActive.copy(alpha = 0.3f))
            ) {
                Text(
                    text = stringResource(id = R.string.about_app_version),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.OrangeActive,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.about_app_tagline),
                fontSize = 14.sp,
                color = colors.BrownText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            AboutMissionCard()

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.about_app_why_title),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.titleAndCationsColor,
                    modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                )

                AboutFeatureCard(
                    icon = painterResource(id = R.drawable.ic_star),
                    iconContainerColor = colors.SuccessAccent,
                    title = stringResource(id = R.string.about_app_speaking_lab_title),
                    description = stringResource(id = R.string.about_app_speaking_lab_desc)
                )

                Spacer(modifier = Modifier.height(12.dp))

                AboutFeatureCard(
                    icon = painterResource(id = R.drawable.ic_camera),
                    iconContainerColor = colors.OrangeActive,
                    title = stringResource(id = R.string.about_app_camera_quests_title),
                    description = stringResource(id = R.string.about_app_camera_quests_desc)
                )

                Spacer(modifier = Modifier.height(12.dp))

                AboutFeatureCard(
                    icon = painterResource(id = R.drawable.ic_medal),
                    iconContainerColor = colors.Amber,
                    title = stringResource(id = R.string.about_app_gamified_title),
                    description = stringResource(id = R.string.about_app_gamified_desc)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.about_app_community_title),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.titleAndCationsColor,
                    modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = LinguaQuestTheme.colors.ProfileCardColor,
                    border = BorderStroke(1.dp, LinguaQuestTheme.colors.ProfileCardBorderColor)
                ) {
                    Column {
                        CommunityListItem(
                            icon = painterResource(id = R.drawable.ic_star),
                            iconTintColor = colors.Amber,
                            title = stringResource(id = R.string.about_app_rate_store),
                            onClick = onRateAppClick
                        )

                        SectionDivider()

                        CommunityListItem(
                            icon = painterResource(id = R.drawable.ic_profile_world),
                            iconTintColor = colors.OrangeActive,
                            title = stringResource(id = R.string.about_app_official_website),
                            onClick = onWebsiteClick
                        )

                        SectionDivider()

                        CommunityListItem(
                            icon = painterResource(id = R.drawable.ic_lock_icon),
                            iconTintColor = colors.SuccessAccent,
                            title = stringResource(id = R.string.about_app_privacy_policy),
                            onClick = onPrivacyClick
                        )

                        SectionDivider()

                        CommunityListItem(
                            icon = painterResource(id = R.drawable.ic_info_icon),
                            iconTintColor = colors.InfoAccent,
                            title = stringResource(id = R.string.about_app_terms_service),
                            onClick = onTermsClick
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(id = R.string.about_app_footer_made_with),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = colors.titleAndCationsColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(id = R.string.about_app_footer_copyright),
                fontSize = 11.sp,
                color = colors.BrownText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AboutMissionCard(
    modifier: Modifier = Modifier
) {
    val colors = LocalLinguaQuestColors.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = LinguaQuestTheme.colors.ProfileCardColor,
        border = BorderStroke(1.dp, LinguaQuestTheme.colors.ProfileCardBorderColor)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = null,
                    tint = colors.OrangeActive,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(id = R.string.about_app_mission_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.titleAndCationsColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(id = R.string.about_app_mission_desc),
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = colors.BrownText
            )
        }
    }
}

@Composable
private fun AboutFeatureCard(
    icon: Painter,
    iconContainerColor: Color,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    val colors = LocalLinguaQuestColors.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = LinguaQuestTheme.colors.ProfileCardColor,
        border = BorderStroke(1.dp, LinguaQuestTheme.colors.ProfileCardBorderColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconContainerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = colors.whiteColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.titleAndCationsColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = colors.BrownText
                )
            }
        }
    }
}

@Composable
private fun CommunityListItem(
    icon: Painter,
    iconTintColor: Color,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalLinguaQuestColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(iconTintColor.copy(alpha = 0.15f))
                .border(1.dp, iconTintColor.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconTintColor,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = colors.titleAndCationsColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutAppContentPreview() {
    LinguaQuestTheme {
        AboutAppContent(
            onBackClick = {},
            onRateAppClick = {},
            onWebsiteClick = {},
            onPrivacyClick = {},
            onTermsClick = {}
        )
    }
}
