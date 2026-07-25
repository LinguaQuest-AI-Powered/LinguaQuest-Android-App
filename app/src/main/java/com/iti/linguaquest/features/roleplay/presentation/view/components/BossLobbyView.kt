package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.R
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
        mascotSize = 180.dp
    ) {
        Text(
            text = "Boss Stage",
            style = AppTextStyles.AppTitle,
            color = LinguaQuestTheme.colors.OrangeActive
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Meet ${scenario.bossName}",
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
                    text = "Your Objective",
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
                    text = "(Read carefully in your native language before starting)",
                    style = AppTextStyles.Caption,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        AppButton(
            text = "Start Roleplay",
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
                    id = com.iti.linguaquest.features.roleplay.domain.model.ScenarioId.SCENARIO_MARKET_01,
                    worldId = "world_1",
                    bossName = "Haga Sherry",
                    roleDescription = "A friendly but firm old fruit vendor in a bustling Cairo market.",
                    objective = "Buy two apples and a bunch of bananas for less than 50 pounds."
                ),
                onStartClicked = {}
            )
        }
    }
}
