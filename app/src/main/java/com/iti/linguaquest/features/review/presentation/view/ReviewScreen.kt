package com.iti.linguaquest.features.review.presentation.view

import android.speech.tts.TextToSpeech
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.features.review.presentation.contract.ReviewEffect
import com.iti.linguaquest.features.review.presentation.contract.ReviewIntent
import com.iti.linguaquest.features.review.presentation.view.model.toTtsLocale
import com.iti.linguaquest.features.review.presentation.viewmodel.ReviewViewModel
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale
import android.speech.tts.UtteranceProgressListener
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent

@Composable
fun ReviewScreen(
    word: WordEntity,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReviewViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()


    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(Unit) {
        val engine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.ENGLISH
            }
        }
        tts = engine

        onDispose {
            engine.stop()
            engine.shutdown()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(ReviewIntent.LoadReview(word))

        viewModel.effects.collectLatest { effect ->
            when (effect) {
                ReviewEffect.NavigateBack -> onBack()

                is ReviewEffect.SpeakText -> {
                    tts?.language = effect.language.toTtsLocale()

                    tts?.setOnUtteranceProgressListener(object :
                        UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {}
                        override fun onDone(utteranceId: String?) {
                            viewModel.onSpeakingFinished()
                        }

                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) {
                            viewModel.onSpeakingFinished()
                        }
                    })

                    tts?.speak(effect.text, TextToSpeech.QUEUE_FLUSH, null, "review_tts")
                }

                ReviewEffect.StopSpeaking -> {
                    tts?.stop()
                }
            }
        }
    }
    OfflineAwareContent(isOnline = isOnline) {
      ReviewContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
      }
}



