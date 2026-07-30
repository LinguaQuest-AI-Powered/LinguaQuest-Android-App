package com.iti.linguaquest.features.help.presentation.contact.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.help.presentation.contact.contract.ContactUsEffect
import com.iti.linguaquest.features.help.presentation.contact.viewmodel.ContactUsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ContactUsScreen(
    onBack: () -> Unit,
    viewModel: ContactUsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ContactUsEffect.NavigateBack -> onBack()
            }
        }
    }

    ContactUsContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}
