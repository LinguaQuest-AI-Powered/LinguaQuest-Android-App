package com.iti.linguaquest.features.game.presentation.result.view.component

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
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun GameSuccessView(
    xpGained: Int,
    coinsGained: Int,
    currentLevel: Int,
    progressPercent: Float,
    onNextLevelClick: () -> Unit,
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
                text = stringResource(R.string.game_result_success_title),
                style = AppTextStyles.ScreenTitle,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 36.sp,
                color = LinguaQuestTheme.colors.BrownText
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.game_result_success_subtitle),
                style = AppTextStyles.DialogMessage,
                color = LinguaQuestTheme.colors.BrownText,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RewardPill(
                    iconRes = R.drawable.ic_xp,
                    amount = stringResource(R.string.game_result_xp_format, xpGained),
                    label = stringResource(R.string.xp),
                    modifier = Modifier.weight(1f)
                )
                RewardPill(
                    iconRes = R.drawable.ic_coin,
                    amount = stringResource(R.string.game_result_coins_format, coinsGained),
                    label = stringResource(R.string.coins_label),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            LevelProgressSection(
                currentLevel = currentLevel,
                progressPercent = progressPercent
            )

            Spacer(modifier = Modifier.height(40.dp))

            AppButton(
                text = stringResource(R.string.game_result_next_level),
                onClick = onNextLevelClick
            )
        }
    }
}