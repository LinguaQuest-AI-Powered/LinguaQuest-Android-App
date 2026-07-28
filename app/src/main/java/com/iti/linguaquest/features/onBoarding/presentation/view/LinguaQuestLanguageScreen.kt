package com.iti.linguaquest.features.onBoarding.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.IconPosition
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.features.onBoarding.presentation.components.LanguageDropdown
import com.iti.linguaquest.features.onBoarding.presentation.viewModel.languageViewModel.LanguagesViewModel
import com.iti.linguaquest.features.onBoarding.presentation.contract.languageContract.LanguagesEffect
import com.iti.linguaquest.features.onBoarding.presentation.contract.languageContract.LanguagesIntent
import com.iti.linguaquest.features.onBoarding.presentation.contract.languageContract.LanguagesState
import com.iti.linguaquest.core.sharedComponents.ErrorView
import com.iti.linguaquest.core.sharedComponents.LoadingView
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.onBoarding.presentation.components.LanguageMascotVideo
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LanguagesScreen(
    onContinue: () -> Unit,
    viewModel: LanguagesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            if (effect is LanguagesEffect.NavigateToLevelScreen) onContinue()
        }
    }
    OfflineAwareContent(isOnline = isOnline) {

        LanguagesScreenContent(
            state = state,
            onIntent = viewModel::onIntent
        )
    }
}

@Composable
private fun LanguagesScreenContent(
    state: LanguagesState,
    onIntent: (LanguagesIntent) -> Unit
) {
    if (state.isLoading) {
        LoadingView()
        return
    }

    if (state.errorRes != null) {
        ErrorView(
            message = stringResource(state.errorRes)
        )
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
        Spacer(Modifier.height(32.dp))

        LanguageMascotVideo(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = stringResource(R.string.choose_your_languages),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.let_set_up_your_learning_journey),
            style = AppTextStyles.Caption,
            color = LinguaQuestTheme.colors.titleAndCationsColor,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(24.dp))

        LanguageDropdown(
            label = stringResource(R.string.i_speak),
            selectedText = state.nativeLanguage?.name,
            isExpanded = state.isNativeDropdownExpanded,
            onToggle = { onIntent(LanguagesIntent.ToggleNativeDropdown) },
            options = state.availableLanguages,
            onSelect = { onIntent(LanguagesIntent.SelectNativeLanguage(it)) },
            accentColor = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(16.dp))

        LanguageDropdown(
            label = stringResource(R.string.i_want_to_learn),
            selectedText = state.targetLanguage?.name,
            placeholder = stringResource(R.string.select_language),
            isExpanded = state.isTargetDropdownExpanded,
            onToggle = { onIntent(LanguagesIntent.ToggleTargetDropdown) },
            options = state.availableLanguages,
            onSelect = { onIntent(LanguagesIntent.SelectTargetLanguage(it)) },
            accentColor = MaterialTheme.colorScheme.tertiary
        )
        Spacer(Modifier.height(24.dp))

        AppButton(
            text = stringResource(R.string.continue_button),
            onClick = { onIntent(LanguagesIntent.ContinueClicked) },
            enabled = state.isContinueEnabled,
            iconPosition = IconPosition.END,
            icon = painterResource(R.drawable.arrow_right),
            modifier = Modifier.padding(vertical = 24.dp)
        )
    }
}

