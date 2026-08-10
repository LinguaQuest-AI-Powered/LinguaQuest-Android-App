package com.iti.linguaquest.features.profile.presentation.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.Card3DWrapper
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.home.presentation.view.components.ProgressTrack
import com.iti.linguaquest.features.profile.presentation.model.ProfileState

@Composable
fun LearningProgressCard(
    state: ProfileState,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    ledgeHeight: Dp = 4.dp,
    cornerRadius: Dp = 20.dp
) {
    Card3DWrapper(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = LinguaQuestTheme.colors.ProfileCardColor,
        borderColor = LinguaQuestTheme.colors.ProfileCardBorderColor,
        onClick = onClick,
        ledgeHeight = ledgeHeight,
        cornerRadius = cornerRadius
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                state.learningLanguageFlag.let { _ ->
                    Text(
                        text = state.learningLanguageFlag,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.width(10.dp))
                }
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "${stringResource(R.string.learning_label)}:${state.learningLanguageName}",
                        color = LinguaQuestTheme.colors.blackColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = state.proficiencyLabel,
                        color = LinguaQuestTheme.colors.iconsColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            val progress = if (state.targetMilestoneXp == 0) 0f
            else (state.currentMilestoneXp.toFloat() / state.targetMilestoneXp).coerceIn(0f, 1f)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${state.currentMilestoneXp} / ${state.targetMilestoneXp} ${
                        stringResource(R.string.milestone_progress_label)
                    }",
                    color = LinguaQuestTheme.colors.iconsColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LingoSpinningIcon(
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        color = LinguaQuestTheme.colors.blackColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            ProgressTrack(
                progress = progress,
                trackColor = LinguaQuestTheme.colors.progressTrackRemainedColor,
                fillColor = MaterialTheme.colorScheme.tertiary,
                height = 8.dp
            )
        }
    }
}