package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.iti.linguaquest.R
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Brush

@Composable
fun BossLobbyView(scenario: BossScenario, onStartClicked: () -> Unit) {
    AppMascotGradientBox(
        imageRes = R.drawable.lingo_initial_state_voice,
        mascotOverlapHeight = 70.dp,
        mascotSize = 180.dp,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.roleplay_boss_stage),
            style = AppTextStyles.AppTitle,
            color = LinguaQuestTheme.colors.OrangeActive
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.roleplay_meet_boss, scenario.bossName),
            style = AppTextStyles.SectionTitle,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = scenario.roleDescription,
            style = AppTextStyles.Definition,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp, 
                    shape = RoundedCornerShape(16.dp), 
                    spotColor = LinguaQuestTheme.colors.OrangeActive,
                    ambientColor = LinguaQuestTheme.colors.OrangeActive
                )
                .background(
                    color = LinguaQuestTheme.colors.whiteColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            LinguaQuestTheme.colors.OrangeActive, 
                            LinguaQuestTheme.colors.DialogGradientBottomLeft
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.roleplay_your_objective),
                    style = AppTextStyles.SectionTitle,
                    color = LinguaQuestTheme.colors.OrangeActive
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = scenario.objective,
                    style = AppTextStyles.SectionTitle,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.roleplay_read_carefully),
                    style = AppTextStyles.Caption,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        AppButton3D(
            text = stringResource(R.string.roleplay_start_roleplay),
            onClick = onStartClicked,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun BossLobbyViewPreview() {
    LinguaQuestTheme {
        Surface {
            BossLobbyView(
                scenario = BossScenario(
                    id = ScenarioId.SCENARIO_MARKET_01,
                    worldId = "world_1",
                    bossName = "Haga Sherry",
                    roleDescription = "A friendly but firm old fruit vendor in a bustling Cairo market.",
                    objective = "Buy two apples and a bunch of bananas for less than 50 pounds.",
                    voiceName = "KORE"
                ),
                onStartClicked = {}
            )
        }
    }
}
