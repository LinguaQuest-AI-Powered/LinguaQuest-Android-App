package com.iti.linguaquest.features.home.presentation.languages.addlanguages.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.home.presentation.languages.addlanguages.contract.AddLanguagesEffect
import com.iti.linguaquest.features.home.presentation.languages.addlanguages.view.components.AddLanguagesContent
import com.iti.linguaquest.features.home.presentation.languages.addlanguages.viewmodel.AddLanguagesViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddLanguagesScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddLanguagesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                AddLanguagesEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    AddLanguagesContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}
