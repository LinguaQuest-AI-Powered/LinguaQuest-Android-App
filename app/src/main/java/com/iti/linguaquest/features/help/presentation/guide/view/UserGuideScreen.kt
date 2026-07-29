package com.iti.linguaquest.features.help.presentation.guide.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.help.presentation.guide.contract.UserGuideEffect
import com.iti.linguaquest.features.help.presentation.guide.viewmodel.UserGuideViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UserGuideScreen(
    onBack: () -> Unit,
    viewModel: UserGuideViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                UserGuideEffect.NavigateBack -> onBack()
            }
        }
    }

    UserGuideContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}
