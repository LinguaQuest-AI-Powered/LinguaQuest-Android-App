package com.iti.linguaquest.features.editprofile.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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