package com.iti.linguaquest.features.setting.presentation.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.theme.LinguaQuestTheme

data class GuideStep(
    val titleRes: Int,
    val descRes: Int,
    val iconRes: Int
)

@Composable
fun UserGuideContent(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LinguaQuestTheme.colors.isDark

    val steps = remember {
        listOf(
            GuideStep(R.string.user_guide_step1_title, R.string.user_guide_step1_desc, R.drawable.ic_profile_world),
            GuideStep(R.string.user_guide_step2_title, R.string.user_guide_step2_desc, R.drawable.lingo_mic),
            GuideStep(R.string.user_guide_step3_title, R.string.user_guide_step3_desc, R.drawable.lingo_writing),
            GuideStep(R.string.user_guide_step4_title, R.string.user_guide_step4_desc, R.drawable.lingo_lockscreen)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 8.dp, bottom = 24.dp)
    ) {
        LinguaQuestScreenTopBar(
            title = stringResource(id = R.string.about_app_user_guide_title),
            onBackClicked = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            steps.forEach { step ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isDark) Color(0xFF122724) else Color(0xFFECFDF5))
                        .border(1.5.dp, if (isDark) Color(0xFF1A584C) else Color(0xFFA7F3D0), RoundedCornerShape(20.dp))
                        .padding(18.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        painter = painterResource(id = step.iconRes),
                        contentDescription = null,
                        tint = if (isDark) Color(0xFF34D399) else Color(0xFF059669),
                        modifier = Modifier.size(28.dp)
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = stringResource(id = step.titleRes),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color(0xFF34D399) else Color(0xFF059669)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = stringResource(id = step.descRes),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserGuideContentLightPreview() {
    LinguaQuestTheme(darkTheme = false) {
        UserGuideContent(onBackClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun UserGuideContentDarkPreview() {
    LinguaQuestTheme(darkTheme = true) {
        UserGuideContent(onBackClick = {})
    }
}
