package com.iti.linguaquest.features.home.presentation.languages.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.statusBarsPadding
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.sharedComponents.dialog.AppDialog
import com.iti.linguaquest.features.home.presentation.languages.viewmodel.AddLanguagesViewModel
import com.iti.linguaquest.features.home.presentation.languages.component.LanguageSelectionCard
import com.iti.linguaquest.features.home.presentation.languages.contract.AddLanguagesEffect
import com.iti.linguaquest.features.home.presentation.languages.contract.AddLanguagesIntent
import com.iti.linguaquest.features.home.presentation.languages.contract.AddLanguagesState
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.layout.ContentScale
import com.iti.linguaquest.core.sharedComponents.state.StatefulContentContainer
import com.iti.linguaquest.features.home.presentation.languages.component.AddLanguagesContent

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

    if (state.languagePendingRemoval != null) {
        AppDialog(
            title = stringResource(R.string.remove_language_title),
            message = stringResource(R.string.remove_language_message, state.languagePendingRemoval!!.name),
            imageRes = R.drawable.lingo_delete_notification,
            onDismissRequest = { viewModel.onIntent(AddLanguagesIntent.DismissRemoveDialog) },
            primaryButtonText = stringResource(R.string.remove),
            onPrimaryClick = { viewModel.onIntent(AddLanguagesIntent.ConfirmRemoveLanguage) },
            secondaryButtonText = stringResource(R.string.cancel),
            onSecondaryClick = { viewModel.onIntent(AddLanguagesIntent.DismissRemoveDialog) }
        )
    }

    AddLanguagesContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}