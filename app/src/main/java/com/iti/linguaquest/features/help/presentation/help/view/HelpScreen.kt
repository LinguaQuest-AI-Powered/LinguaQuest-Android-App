package com.iti.linguaquest.features.help.presentation.help.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.GlobalUiHostViewModel
import com.iti.linguaquest.core.sharedComponents.dialog.DialogUiState
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.help.presentation.help.contract.HelpEffect
import com.iti.linguaquest.features.help.presentation.help.viewmodel.HelpViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HelpScreen(
    onBack: () -> Unit,
    onNavigateToContactSupport: () -> Unit = {},
    onNavigateToReportBug: () -> Unit = {},
    globalUiHostViewModel: GlobalUiHostViewModel = hiltViewModel(),
    viewModel: HelpViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                HelpEffect.NavigateBack -> onBack()
                HelpEffect.NavigateToContactSupport -> {
                    globalUiHostViewModel.dialogController.show(
                        DialogUiState(
                            title = UiText.StringResource(R.string.help_support_coming_soon_title),
                            message = UiText.StringResource(R.string.help_support_coming_soon_message),
                            confirmText = UiText.StringResource(R.string.help_support_coming_soon_action)
                        )
                    )
                }
                HelpEffect.NavigateToReportBug -> {
                    globalUiHostViewModel.dialogController.show(
                        DialogUiState(
                            title = UiText.StringResource(R.string.help_support_coming_soon_title),
                            message = UiText.StringResource(R.string.help_support_coming_soon_message),
                            confirmText = UiText.StringResource(R.string.help_support_coming_soon_action)
                        )
                    )
                }
            }
        }
    }

    HelpContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}
