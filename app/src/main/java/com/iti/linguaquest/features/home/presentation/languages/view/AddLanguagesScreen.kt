package com.iti.linguaquest.features.home.presentation.languages.view

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
import androidx.compose.foundation.lazy.grid.GridCells
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.statusBarsPadding
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
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

@Composable
fun AddLanguagesContent(
    state: AddLanguagesState,
    onIntent: (AddLanguagesIntent) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            LinguaQuestScreenTopBar(
                title = stringResource(R.string.add_languages_title),
                onBackClicked = { onIntent(AddLanguagesIntent.BackClicked) },
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                AppButton(
                    text = stringResource(R.string.add_selected_format, state.selectedLanguageIds.size),
                    onClick = { onIntent(AddLanguagesIntent.AddSelectedClicked) },
                    enabled = state.selectedLanguageIds.isNotEmpty()
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.add_languages_select_instruction),
                style = AppTextStyles.LessonTitle.copy(
                    fontWeight = FontWeight.Normal,
                    color = LinguaQuestTheme.colors.titleAndCationsColor
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { onIntent(AddLanguagesIntent.SearchQueryChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(R.string.add_languages_search_placeholder),
                        style = AppTextStyles.Translation,
                        color = LinguaQuestTheme.colors.textFieldPlaceholder
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.cd_search),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = LinguaQuestTheme.colors.whiteColor,
                    unfocusedContainerColor = LinguaQuestTheme.colors.whiteColor,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = LinguaQuestTheme.colors.textFieldBorder
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    focusManager.clearFocus()
                }),
                textStyle = AppTextStyles.Translation.copy(color = LinguaQuestTheme.colors.titleAndCationsColor)
            )

            Spacer(modifier = Modifier.height(24.dp))

            val filteredLanguages = state.availableLanguages.filter {
                it.name.contains(state.searchQuery, ignoreCase = true)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = filteredLanguages,
                    key = { it.id }
                ) { language ->
                    LanguageSelectionCard(
                        language = language,
                        isSelected = state.selectedLanguageIds.contains(language.id),
                        onClick = { onIntent(AddLanguagesIntent.LanguageToggled(language.id)) },
                        modifier = Modifier.height(180.dp)
                    )
                }
            }
        }
    }
}