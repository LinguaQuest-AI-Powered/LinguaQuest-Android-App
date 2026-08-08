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
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.AppConfettiView
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.AppRewardsRow
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun GameSuccessView(
    xpGained: Int,
    coinsGained: Int,
    currentLevel: Int,
    progressPercent: Float,
    onNextLevelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AppConfettiView()

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

            AppRewardsRow(
                coinsAmount = coinsGained,
                xpAmount = xpGained
            )

            Spacer(modifier = Modifier.height(32.dp))

            LevelProgressSection(
                currentLevel = currentLevel,
                progressPercent = progressPercent
            )

            Spacer(modifier = Modifier.height(40.dp))

            AppButton3D(
                text = stringResource(R.string.game_result_next_level),
                onClick = onNextLevelClick
            )
        }
    }
}