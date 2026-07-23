package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayPhase

@Composable
fun LingoRoleplayAvatar(
    phase: RoleplayPhase,
    isPassed: Boolean = true,
    modifier: Modifier = Modifier,
    size: Dp = 180.dp
) {
    val imageRes = when (phase) {
        RoleplayPhase.LOBBY -> R.drawable.lingo_initial_state_voice
        RoleplayPhase.IDLE -> R.drawable.lingo_initial_state_voice
        RoleplayPhase.RECORDING -> R.drawable.lingo_mic
        RoleplayPhase.PROCESSING -> R.drawable.lingo_checking_pronounciation
        RoleplayPhase.AI_SPEAKING -> R.drawable.lingo_new_password
        RoleplayPhase.OUTCOME -> if (isPassed) R.drawable.lingo_success else R.drawable.lingo_error
    }

    Image(
        painter = painterResource(imageRes),
        contentDescription = "Lingo",
        modifier = modifier
            .size(size)
            .clip(CircleShape)
    )
}

/** Returns the drawable resource for the given phase, for use with [AppMascotGradientBox]. */
fun lingoImageForPhase(phase: RoleplayPhase, isPassed: Boolean = true): Int = when (phase) {
    RoleplayPhase.LOBBY -> R.drawable.lingo_initial_state_voice
    RoleplayPhase.IDLE -> R.drawable.lingo_initial_state_voice
    RoleplayPhase.RECORDING -> R.drawable.lingo_mic
    RoleplayPhase.PROCESSING -> R.drawable.lingo_checking_pronounciation
    RoleplayPhase.AI_SPEAKING -> R.drawable.lingo_new_password
    RoleplayPhase.OUTCOME -> if (isPassed) R.drawable.lingo_success else R.drawable.lingo_error
}
