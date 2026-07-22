package com.iti.linguaquest.features.lockscreen.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ShareTopBar
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenWordDetailIntent
import com.iti.linguaquest.features.lockscreen.presentation.viewmodel.LockScreenWordDetailViewModel

@Composable
fun LockScreenWordDetailScreen(
    wordId: Int,
    onBack: () -> Unit,
    viewModel: LockScreenWordDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(wordId) {
        viewModel.setWordId(wordId)
    }

    val backgroundBrush = Brush.verticalGradient(
        listOf(
            LinguaQuestTheme.colors.ChipBackground.copy(alpha = 0.35f),
            MaterialTheme.colorScheme.background,
            LinguaQuestTheme.colors.Amber.copy(alpha = 0.12f)
        )
    )

    Scaffold(
        topBar = {
            ShareTopBar(
                title = R.string.lockscreen_vocabulary_word_detail_title,
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when {
                    state.isLoading -> LoadingCard()
                    state.errorMessage != null -> ErrorCard(
                        message = state.errorMessage.orEmpty(),
                        onRetry = { viewModel.onIntent(LockScreenWordDetailIntent.Retry) }
                    )
                    state.word != null -> WordDetailCard(word = state.word!!)
                }

                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.OrangeActive,
                        contentColor = AppColors.White
                    )
                ) {
                    Text(stringResource(R.string.lockscreen_vocabulary_back), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun LoadingCard() {
    Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = LinguaQuestTheme.colors.ChipBackground,
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lock_icon),
                        contentDescription = null,
                        tint = AppColors.OrangeActive,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Text(
                text = stringResource(R.string.lockscreen_vocabulary_loading),
                style = MaterialTheme.typography.titleMedium,
                color = LinguaQuestTheme.colors.titleAndCationsColor
            )
        }
    }
}

@Composable
private fun ErrorCard(message: String, onRetry: () -> Unit) {
    Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = LinguaQuestTheme.colors.ErrorAccent.copy(alpha = 0.08f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = message,
                color = LinguaQuestTheme.colors.ErrorAccent,
                style = MaterialTheme.typography.bodyLarge
            )
            OutlinedButton(onClick = onRetry) {
                Text(stringResource(R.string.lockscreen_vocabulary_retry))
            }
        }
    }
}

@Composable
private fun WordDetailCard(word: LockScreenWord) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = LinguaQuestTheme.colors.ChipBackground,
                    modifier = Modifier.size(54.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_learning_language),
                            contentDescription = null,
                            tint = AppColors.OrangeActive,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.lockscreen_vocabulary_word_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = word.word,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = LinguaQuestTheme.colors.titleAndCationsColor
                    )
                }
            }

            StatusBadge(word.status.name)

            LabelValueCard(
                label = stringResource(R.string.lockscreen_vocabulary_translation_label),
                value = word.translation
            )

            LabelValueCard(
                label = stringResource(R.string.lockscreen_vocabulary_example_label),
                value = word.exampleSentence,
                emphasize = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MiniInfoChip(
                    label = word.targetLanguage,
                    iconRes = R.drawable.ic_learning_language
                )
                MiniInfoChip(
                    label = word.proficiencyLevel,
                    iconRes = R.drawable.ic_timer
                )
            }
        }
    }
}

@Composable
private fun LabelValueCard(
    label: String,
    value: String,
    emphasize: Boolean = false
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = if (emphasize) {
            LinguaQuestTheme.colors.ChipBackground.copy(alpha = 0.7f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = if (emphasize) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
                fontWeight = if (emphasize) FontWeight.SemiBold else FontWeight.Normal,
                color = LinguaQuestTheme.colors.titleAndCationsColor
            )
        }
    }
}

@Composable
private fun StatusBadge(text: String) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = AppColors.ChipBackground,
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(AppColors.OrangeActive)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = LinguaQuestTheme.colors.titleAndCationsColor
            )
        }
    }
}

@Composable
private fun MiniInfoChip(label: String, iconRes: Int) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = AppColors.OrangeActive,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = LinguaQuestTheme.colors.titleAndCationsColor
            )
        }
    }
}
