package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.game.presentation.result.view.component.RewardPill
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayAssessmentResult
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun BossSuccessView(
    result: RoleplayAssessmentResult,
    onAdvanceToNextWorld: () -> Unit,
    modifier: Modifier = Modifier
) {
    val confettiColors = listOf(
        LinguaQuestTheme.colors.OrangeActive.toArgb(),
        LinguaQuestTheme.colors.splashTopLeftColor.toArgb(),
        LinguaQuestTheme.colors.whiteColor.toArgb()
    )

    val party = Party(
        speed = 0f,
        maxSpeed = 30f,
        damping = 0.9f,
        spread = 360,
        colors = confettiColors,
        position = Position.Relative(0.5, 0.25),
        emitter = Emitter(duration = 200, TimeUnit.MILLISECONDS).max(200)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        KonfettiView(
            modifier = Modifier.fillMaxSize(),
            parties = listOf(party)
        )

        AppMascotGradientBox(
            imageRes = R.drawable.lingo_success,
            mascotSize = 200.dp,
            mascotOverlapHeight = 70.dp
        ) {

            Text(
                text = "Victory!",
                style = AppTextStyles.ScreenTitle,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 36.sp,
                color = LinguaQuestTheme.colors.BrownText
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Fluency Score: ${result.fluencyScore}/100",
                style = AppTextStyles.DialogMessage,
                fontWeight = FontWeight.Bold,
                color = LinguaQuestTheme.colors.BrownText,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = LinguaQuestTheme.colors.whiteColor.copy(alpha = 0.5f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = result.feedbackMessage,
                    style = AppTextStyles.DialogMessage,
                    color = LinguaQuestTheme.colors.BrownText,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RewardPill(
                    iconRes = R.drawable.ic_xp,
                    amount = "+150",
                    label = "XP",
                    modifier = Modifier.weight(1f)
                )
                RewardPill(
                    iconRes = R.drawable.ic_coin,
                    amount = "+50",
                    label = "Coins",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            AppButton(
                text = "Next World",
                onClick = onAdvanceToNextWorld
            )
        }
    }
}
