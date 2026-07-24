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
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayAssessmentResult

@Composable
fun BossResultView(
    result: RoleplayAssessmentResult, 
    onAdvanceToNextWorld: () -> Unit,
    onRetryStage: () -> Unit
) {
    val isSuccess = result.isTaskCompleted
    val title = if (isSuccess) "Victory!" else "Stage Failed"
    val titleColor = if (isSuccess) LinguaQuestTheme.colors.OrangeActive else LinguaQuestTheme.colors.ErrorAccent
    
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = titleColor
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Fluency Score: ${result.fluencyScore}/100",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = result.feedbackMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (isSuccess) {
                    Text(
                        text = "Rewards: +150 XP, +50 Coins",
                        style = MaterialTheme.typography.titleMedium,
                        color = LinguaQuestTheme.colors.OrangeActive,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "Rewards: 0 XP",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        if (isSuccess) {
            Button(
                onClick = onAdvanceToNextWorld,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Next World", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Button(
                onClick = onRetryStage,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Try Again", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
