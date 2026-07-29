package com.iti.linguaquest.features.help.presentation.faqs.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.help.presentation.faqs.contract.FaqsEffect
import com.iti.linguaquest.features.help.presentation.faqs.viewmodel.FaqsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FaqsScreen(
    onBack: () -> Unit,
    viewModel: FaqsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                FaqsEffect.NavigateBack -> onBack()
            }
        }
    }

    FaqsContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}
