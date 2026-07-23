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

@Composable
fun LingoRoleplayAvatar(
    isAiSpeaking: Boolean,
    isUserSpeaking: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 180.dp
) {
    val imageRes = lingoImageForState(isAiSpeaking, isUserSpeaking, isLoading)

    Image(
        painter = painterResource(imageRes),
        contentDescription = "Lingo",
        modifier = modifier
            .size(size)
            .clip(CircleShape)
    )
}

/** Returns the drawable resource for the given live states. */
fun lingoImageForState(isAiSpeaking: Boolean, isUserSpeaking: Boolean, isLoading: Boolean): Int {
    return when {
        isLoading -> R.drawable.lingo_checking_pronounciation
        isUserSpeaking -> R.drawable.lingo_mic
        isAiSpeaking -> R.drawable.lingo_new_password
        else -> R.drawable.lingo_initial_state_voice
    }
}
