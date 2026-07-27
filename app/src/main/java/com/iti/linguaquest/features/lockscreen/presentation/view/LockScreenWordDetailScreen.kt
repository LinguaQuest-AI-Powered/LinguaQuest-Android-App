package com.iti.linguaquest.features.lockscreen.presentation.view

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.dialog.AppDialog
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components.CoinRainOverlay
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import com.iti.linguaquest.features.lockscreen.presentation.view.component.ErrorCard
import com.iti.linguaquest.features.lockscreen.presentation.view.component.CustomTopBar
import com.iti.linguaquest.features.lockscreen.presentation.view.component.EmptyVaultState
import com.iti.linguaquest.features.lockscreen.presentation.view.component.LargeWordCard
import com.iti.linguaquest.features.lockscreen.presentation.view.component.VaultListMode
import com.iti.linguaquest.features.lockscreen.presentation.viewmodel.LockScreenWordDetailViewModel

private const val MILESTONE_STEP = 10
private const val COINS_PER_MILESTONE = 100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockScreenWordDetailScreen(
    wordId: Int,
    onBack: () -> Unit,
    onNavigateToReview: (LockScreenWord) -> Unit,
    viewModel: LockScreenWordDetailViewModel = hiltViewModel()
) {
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    val state by viewModel.state.collectAsStateWithLifecycle()
    var showListMode by remember { mutableStateOf(wordId <= 0) }
    var searchQuery by remember { mutableStateOf("") }
    var showMilestoneDialog by remember { mutableStateOf(false) }
    var lastMilestoneCount by remember { mutableStateOf(0) }

     LaunchedEffect(state.words.size) {
        val count = state.words.size
        if (count == 0) {
            lastMilestoneCount = 0
        } else if (count % MILESTONE_STEP == 0 && count != lastMilestoneCount) {
            lastMilestoneCount = count
            showMilestoneDialog = true
        }
    }

    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(context) {
        val engine = TextToSpeech(context) { _ -> }
        tts = engine
        onDispose {
            engine.stop()
            engine.shutdown()
        }
    }

    LaunchedEffect(wordId) {
        viewModel.setHighlightedWordId(wordId.takeIf { it > 0 })
    }

    Scaffold(
        topBar = {
            CustomTopBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.requestNewWord()
                    showListMode = false
                },
                containerColor = Color(0xFF915900),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.cd_add)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AppColors.OrangeActive)
                    }
                }

                state.words.isEmpty() -> {
                    EmptyVaultState(modifier = Modifier.fillMaxSize())
                }

                else -> {
                    OfflineAwareContent(isOnline = isOnline) {

                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Header
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = stringResource(R.string.lockscreen_vocabulary_vault_title),
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = LinguaQuestTheme.colors.titleAndCationsColor
                                    )
                                    Text(
                                        text = stringResource(
                                            R.string.lockscreen_vocabulary_words_collected,
                                            state.words.size
                                        ),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = LinguaQuestTheme.colors.titleAndCationsColor.copy(
                                            alpha = 0.7f
                                        )
                                    )
                                }
                            }

                            if (state.errorMessage != null) {
                                ErrorCard(
                                    message = state.errorMessage.orEmpty()
                                )
                            }

                            if (!showListMode) {
                                val currentWord =
                                    state.words.find { it.id == state.highlightedWordId }
                                        ?: state.words.firstOrNull()

                                if (currentWord != null) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 32.dp)
                                    ) {
                                        LargeWordCard(
                                            word = currentWord,
                                            onGotItClick = { showListMode = true }
                                        )
                                    }
                                } else {
                                    EmptyVaultState(modifier = Modifier.fillMaxSize())
                                }
                            } else {
                                VaultListMode(
                                    words = state.words,
                                    searchQuery = searchQuery,
                                    onSearchQueryChange = { searchQuery = it },
                                    onNavigateToReview = onNavigateToReview,
                                    tts = tts
                                )
                            }
                        }
                    }
                }
            }
             if (showMilestoneDialog) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CoinRainOverlay(modifier = Modifier.fillMaxSize())
                    AppDialog(
                        title = stringResource(R.string.lockscreen_milestone_dialog_title),
                        message = stringResource(
                            R.string.lockscreen_milestone_dialog_message,
                            lastMilestoneCount,
                            COINS_PER_MILESTONE
                        ),
                        imageRes = R.drawable.lingo_reward,
                        showCloseIcon = true,
                        onDismissRequest = { showMilestoneDialog = false },
                        primaryButtonText = stringResource(R.string.lockscreen_milestone_dialog_button),
                        onPrimaryClick = { showMilestoneDialog = false }
                    )
                }
            }
        }
    }
}
