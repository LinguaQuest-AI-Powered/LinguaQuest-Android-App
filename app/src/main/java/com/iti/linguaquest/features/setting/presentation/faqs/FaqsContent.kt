package com.iti.linguaquest.features.setting.presentation.faqs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBarBackButtonStyle
import com.iti.linguaquest.core.theme.LinguaQuestTheme

data class FaqItem(
    val questionRes: Int,
    val answerRes: Int
)

@Composable
fun FaqsContent(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LinguaQuestTheme.colors.isDark

    val faqItems = remember {
        listOf(
            FaqItem(R.string.faqs_q1, R.string.faqs_a1),
            FaqItem(R.string.faqs_q2, R.string.faqs_a2),
            FaqItem(R.string.faqs_q3, R.string.faqs_a3),
            FaqItem(R.string.faqs_q4, R.string.faqs_a4)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 8.dp, bottom = 24.dp)
    ) {
        LinguaQuestScreenTopBar(
            title = stringResource(id = R.string.about_app_faqs_title),
            onBackClicked = onBackClick,
            isTitleCentered = true,
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            titleColor = com.iti.linguaquest.core.theme.LocalLinguaQuestColors.current.BrownText,
            titleTextStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 20.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            ),
            showDivider = true,
            dividerSpacing = 16.dp,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 22.dp, vertical = 0.dp),
            backButtonStyle = LinguaQuestScreenTopBarBackButtonStyle.Circular,
            backButtonSize = 40.dp,
            backButtonBackgroundColor = com.iti.linguaquest.core.theme.LocalLinguaQuestColors.current.whiteColor,
            backButtonContentColor = com.iti.linguaquest.core.theme.LocalLinguaQuestColors.current.OrangeActive,
            backButtonIconSize = 18.dp
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            faqItems.forEach { faq ->
                var expanded by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isDark) Color(0xFF24221A) else Color(0xFFFFF9EE))
                        .border(1.5.dp, if (isDark) Color(0xFF5A4D27) else Color(0xFFFDE68A), RoundedCornerShape(20.dp))
                        .clickable { expanded = !expanded }
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = faq.questionRes),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color(0xFFEAB308) else Color(0xFFD97706),
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = if (isDark) Color(0xFFEAB308) else Color(0xFFD97706),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    AnimatedVisibility(visible = expanded) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = stringResource(id = faq.answerRes),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FaqsContentLightPreview() {
    LinguaQuestTheme(darkTheme = false) {
        FaqsContent(onBackClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun FaqsContentDarkPreview() {
    LinguaQuestTheme(darkTheme = true) {
        FaqsContent(onBackClick = {})
    }
}
