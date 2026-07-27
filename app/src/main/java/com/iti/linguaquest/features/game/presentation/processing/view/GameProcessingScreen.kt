package com.iti.linguaquest.features.game.presentation.processing.view

import android.content.Context
import android.net.Uri
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.game.presentation.processing.contract.GameProcessingEffect
import com.iti.linguaquest.features.game.presentation.processing.contract.GameProcessingIntent
import com.iti.linguaquest.features.game.presentation.processing.contract.GameWhackIntent
import com.iti.linguaquest.features.game.presentation.processing.view.component.GameProcessingView
import com.iti.linguaquest.features.game.presentation.processing.view.component.GameWhackView
import com.iti.linguaquest.features.game.presentation.processing.viewmodel.GameProcessingViewModel
import com.iti.linguaquest.features.game.presentation.processing.viewmodel.GameWhackViewModel
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel
import com.iti.linguaquest.features.game.presentation.shared.VerificationOutcome
import java.io.File

@Composable
fun GameProcessingScreen(
    sharedViewModel: GameSharedViewModel,
    onNavigateToResult: () -> Unit,
    modifier: Modifier = Modifier,
    processingViewModel: GameProcessingViewModel = hiltViewModel(),
    whackViewModel: GameWhackViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val whackState by whackViewModel.state.collectAsState()
    val sharedState by sharedViewModel.sharedState.collectAsState()
    val isOnline by sharedViewModel.isOnline.collectAsStateWithLifecycle()

    LaunchedEffect(sharedState.capturedImageUri, isOnline) {
        val uri = sharedState.capturedImageUri
        if (!isOnline) {
            sharedViewModel.setVerificationOutcome(
                VerificationOutcome.Error(
                    errorMessage = UiText.StringResource(R.string.no_internet_title)
                )
            )
            onNavigateToResult()
        } else if (uri != null) {
            val file = uriToFile(context, uri)
            processingViewModel.verifyImage(
                worldId = sharedState.worldId,
                levelId = sharedState.levelId,
                imageFile = file
            )
        } else {
            processingViewModel.onIntent(GameProcessingIntent.StartProcessing)
        }
    }

    LaunchedEffect(Unit) {
        processingViewModel.effect.collect { effect ->
            val outcome = when (effect) {
                is GameProcessingEffect.NavigateToSuccess -> {
                    VerificationOutcome.Success(
                        xpAwarded = effect.xp,
                        coinsAwarded = effect.coins + whackState.currentCoins
                    )
                }
                is GameProcessingEffect.NavigateToFailure -> {
                    VerificationOutcome.Failure(reason = effect.reason)
                }
                is GameProcessingEffect.NavigateToError -> {
                    VerificationOutcome.Error(errorMessage = effect.errorMessage)
                }
            }
            sharedViewModel.setVerificationOutcome(outcome)
            onNavigateToResult()
        }
    }

    OfflineAwareContent(isOnline = isOnline, modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LinguaQuestTheme.colors.blackColor.copy(alpha = 0.5f))
        ) {

            Crossfade(
                targetState = whackState.isGameActive,
                label = "ProcessingToGameCrossfade"
            ) { isGameActive ->
                if (isGameActive) {
                    GameWhackView(
                        state = whackState,
                        onLingoWhacked = { whackViewModel.onIntent(GameWhackIntent.LingoWhacked) },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    GameProcessingView(
                        imageUri = sharedState.capturedImageUri,
                        onStartGameClicked = { whackViewModel.onIntent(GameWhackIntent.StartGame) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

private fun uriToFile(context: Context, uri: Uri): File? {
    return try {
        if (uri.scheme == "file" && !uri.path.isNullOrEmpty()) {
            File(uri.path!!)
        } else {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File(context.cacheDir, "verify_upload_${System.currentTimeMillis()}.jpg")
            tempFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            tempFile
        }
    } catch (e: Exception) {
        null
    }
}
