package com.iti.linguaquest.features.roleplay.presentation.view.contents

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayState
import com.iti.linguaquest.features.roleplay.presentation.view.components.AiSpeechBubble
import com.iti.linguaquest.features.roleplay.presentation.view.components.LingoRoleplayAvatar
import com.iti.linguaquest.features.roleplay.presentation.viewModel.RoleplayViewModel

@Composable
fun AiSpeakingPhaseContent(state: RoleplayState, viewModel: RoleplayViewModel) {
    Text(
        "Turn ${state.turnCount}/${state.maxTurns}",
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(Modifier.height(12.dp))

    AiSpeechBubble(
        aiText = state.aiResponseText,
        translation = state.aiTranslation
    )

    Spacer(Modifier.height(16.dp))

    val infiniteTransition = rememberInfiniteTransition(label = "speaking")
    val speakingScale by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            tween(500, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "speakingScale"
    )

    LingoRoleplayAvatar(
        phase = state.phase,
        modifier = Modifier.scale(speakingScale)
    )

    Spacer(Modifier.height(12.dp))

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { i ->
            val dotTransition = rememberInfiniteTransition(label = "dot_$i")
            val dotScale by dotTransition.animateFloat(
                initialValue = 0.6f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    tween(400, delayMillis = i * 150, easing = FastOutSlowInEasing),
                    RepeatMode.Reverse
                ),
                label = "dotScale_$i"
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .scale(dotScale)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
            if (i < 2) Spacer(Modifier.width(4.dp))
        }
        Spacer(Modifier.width(8.dp))
        Text(
            "Speaking...",
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
    Spacer(Modifier.height(16.dp))
}
