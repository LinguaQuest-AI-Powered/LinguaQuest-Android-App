package com.iti.linguaquest.features.home.presentation.languages.addlanguages.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.sharedComponents.dialog.AppDialog
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.home.presentation.languages.addlanguages.contract.AddLanguagesIntent
import com.iti.linguaquest.features.home.presentation.languages.addlanguages.contract.AddLanguagesState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLanguagesContent(
    state: AddLanguagesState,
    onIntent: (AddLanguagesIntent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    if (state.languagePendingRemoval != null) {
        AppDialog(
            title = stringResource(R.string.remove_language_title),
            message = stringResource(R.string.remove_language_message, state.languagePendingRemoval.name),
            onDismissRequest = { onIntent(AddLanguagesIntent.DismissRemoveDialog) },
            primaryButtonText = stringResource(R.string.remove),
            onPrimaryClick = { onIntent(AddLanguagesIntent.ConfirmRemoveLanguage) },
            secondaryButtonText = stringResource(R.string.cancel),
            onSecondaryClick = { onIntent(AddLanguagesIntent.DismissRemoveDialog) }
        )
    }

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
                AppButton3D(
                    text = stringResource(R.string.add_selected_format, state.selectedLanguageIds.size),
                    onClick = { onIntent(AddLanguagesIntent.AddSelectedClicked) },
                    enabled = state.selectedLanguageIds.isNotEmpty(),
                    isLoading = state.isLoading && state.selectedLanguageIds.isNotEmpty()
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

            if (state.isLoading && state.availableLanguages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LingoSpinningIcon(size = 36.dp)
                }
            } else if (filteredLanguages.isEmpty() && state.searchQuery.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(bottom = 64.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.lingo_empty),
                            contentDescription = null,
                            modifier = Modifier.size(350.dp),
                            contentScale = androidx.compose.ui.layout.ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = stringResource(R.string.add_languages_no_results),
                            style = AppTextStyles.ScreenTitle.copy(
                                fontWeight = FontWeight.Bold,
                                color = LinguaQuestTheme.colors.titleAndCationsColor,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            } else {
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
                        if (language.isAdded) {
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { dismissValue ->
                                    if (dismissValue == SwipeToDismissBoxValue.EndToStart || dismissValue == SwipeToDismissBoxValue.StartToEnd) {
                                        onIntent(AddLanguagesIntent.RequestRemoveLanguage(language))
                                        true
                                    } else {
                                        false
                                    }
                                }
                            )

                            LaunchedEffect(state.languagePendingRemoval) {
                                if (state.languagePendingRemoval == null && dismissState.currentValue != SwipeToDismissBoxValue.Settled) {
                                    dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                                }
                            }

                            SwipeToDismissBox(
                                state = dismissState,
                                modifier = Modifier
                                    .height(180.dp)
                                    .animateItem(),
                                backgroundContent = {
                                    val color = MaterialTheme.colorScheme.error
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(24.dp))
                                            .background(color),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = stringResource(R.string.remove),
                                            tint = Color.White,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                },
                                content = {
                                    LanguageSelectionCard(
                                        language = language,
                                        isSelected = state.selectedLanguageIds.contains(language.id),
                                        onClick = { onIntent(AddLanguagesIntent.LanguageToggled(language.id)) },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            )
                        } else {
                            LanguageSelectionCard(
                                language = language,
                                isSelected = state.selectedLanguageIds.contains(language.id),
                                onClick = { onIntent(AddLanguagesIntent.LanguageToggled(language.id)) },
                                modifier = Modifier
                                    .height(180.dp)
                                    .animateItem()
                            )
                        }
                    }
                }
            }
        }
    }
}
