package com.iti.linguaquest.features.help.presentation.help.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.help.presentation.help.contract.HelpEffect
import com.iti.linguaquest.features.help.presentation.help.viewmodel.HelpViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HelpScreen(
    onBack: () -> Unit,
    onOpenFaqs: () -> Unit,
    onOpenContactUs: () -> Unit,
    onOpenUserGuide: () -> Unit,
    viewModel: HelpViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                HelpEffect.NavigateBack -> onBack()
                HelpEffect.NavigateToFaqs -> onOpenFaqs()
                HelpEffect.NavigateToContactUs -> onOpenContactUs()
                HelpEffect.NavigateToUserGuide -> onOpenUserGuide()
            }
        }
    }

    HelpContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}
