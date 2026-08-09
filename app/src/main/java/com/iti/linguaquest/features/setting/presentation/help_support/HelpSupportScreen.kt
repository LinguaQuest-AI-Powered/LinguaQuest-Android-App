package com.iti.linguaquest.features.setting.presentation.help_support

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.GlobalUiHostViewModel
import com.iti.linguaquest.core.sharedComponents.dialog.DialogUiState
import com.iti.linguaquest.core.sharedComponents.text.UiText

@Composable
fun HelpSupportScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val globalUiHostViewModel: GlobalUiHostViewModel = hiltViewModel()

    HelpSupportContent(
        onBackClick = onBack,
        onComingSoonClick = {
            globalUiHostViewModel.dialogController.show(
                DialogUiState(
                    title = UiText.StringResource(R.string.help_support_coming_soon_title),
                    message = UiText.StringResource(R.string.help_support_coming_soon_message),
                    confirmText = UiText.StringResource(R.string.help_support_coming_soon_action)
                )
            )
        },
        modifier = modifier
    )
}
