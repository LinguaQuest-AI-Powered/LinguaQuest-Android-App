package com.iti.linguaquest.features.setting.presentation.help_support

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBarBackButtonStyle
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import androidx.core.net.toUri


@Composable
fun HelpSupportContent(
    onBackClick: () -> Unit,
    onComingSoonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LinguaQuestTheme.colors.isDark
    val context = LocalContext.current

    var subjectText by remember { mutableStateOf("") }
    var messageText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 8.dp, bottom = 24.dp)
    ) {
        LinguaQuestScreenTopBar(
            title = stringResource(id = R.string.settings_help_support),
            onBackClicked = onBackClick,
            isTitleCentered = true,
            containerColor = Color.Transparent,
            titleColor = LocalLinguaQuestColors.current.BrownText,
            titleTextStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            showDivider = true,
            dividerSpacing = 16.dp,
            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 0.dp),
            backButtonStyle = LinguaQuestScreenTopBarBackButtonStyle.Circular,
            backButtonSize = 40.dp,
            backButtonBackgroundColor = LocalLinguaQuestColors.current.whiteColor,
            backButtonContentColor = LocalLinguaQuestColors.current.OrangeActive,
            backButtonIconSize = 18.dp
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.help_hero_title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalLinguaQuestColors.current.titleAndCationsColor
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = stringResource(id = R.string.help_hero_subtitle),
                        fontSize = 14.sp,
                        color = LocalLinguaQuestColors.current.BrownText
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Image(
                    painter = painterResource(id = R.drawable.lingo_mail),
                    contentDescription = null,
                    modifier = Modifier.size(110.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isDark) Color(0xFF13222A) else Color(0xFFF0FAF9))
                    .border(1.5.dp, if (isDark) Color(0xFF1B4E57) else Color(0xFF99F6E4), RoundedCornerShape(20.dp))
                    .clickable {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = "mailto:support@linguaquest.com".toUri()
                        }
                        context.startActivity(intent)
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isDark) Color(0xFF153843) else Color(0xFFCCFBF1)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.email),
                        contentDescription = null,
                        tint = if (isDark) Color(0xFF2DD4BF) else Color(0xFF0D9488),
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = stringResource(id = R.string.contact_email_label),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color(0xFF2DD4BF) else Color(0xFF0D9488)
                    )

                    Text(
                        text = stringResource(id = R.string.help_contact_detail),
                        fontSize = 13.sp,
                        color = LocalLinguaQuestColors.current.BrownText
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.contact_us_subject_label),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LocalLinguaQuestColors.current.titleAndCationsColor
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = subjectText,
                onValueChange = { subjectText = it },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.contact_us_subject_placeholder),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = LinguaQuestTheme.colors.textFieldBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.contact_us_message_label),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LocalLinguaQuestColors.current.titleAndCationsColor
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.contact_us_message_placeholder),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                },
                minLines = 4,
                maxLines = 6,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = LinguaQuestTheme.colors.textFieldBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(28.dp))

            AppButton3D(
                text = stringResource(id = R.string.contact_us_send_button),
                onClick = {
                    onComingSoonClick()
                },
                textColor = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HelpSupportContentLightPreview() {
    LinguaQuestTheme(darkTheme = false) {
        HelpSupportContent(
            onBackClick = {},
            onComingSoonClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HelpSupportContentDarkPreview() {
    LinguaQuestTheme(darkTheme = true) {
        HelpSupportContent(
            onBackClick = {},
            onComingSoonClick = {}
        )
    }
}
