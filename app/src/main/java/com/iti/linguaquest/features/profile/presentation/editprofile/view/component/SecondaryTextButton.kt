package com.iti.linguaquest.features.profile.presentation.editprofile.view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme


@Composable
fun SecondaryTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = AppColors.BrownText
        )
    }
}

@Composable
fun DisplayNameTrailingIcon() {
    Icon(
        imageVector = Icons.Outlined.Person,
        contentDescription = null,
        tint = LinguaQuestTheme.colors.textFieldBorder
    )
}

@Composable
fun TaglineTrailingIcon() {
    Icon(
        imageVector = Icons.Outlined.Create,
        contentDescription = null,
        tint = LinguaQuestTheme.colors.textFieldBorder
    )
}

@Composable
fun PasswordVisibilityToggleIcon(
    isVisible: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = if (isVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
        contentDescription = if (isVisible) "Hide password" else "Show password",
        tint = LinguaQuestTheme.colors.textFieldBorder,
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = false, radius = 18.dp),
            onClick = onToggle
        )
    )
}