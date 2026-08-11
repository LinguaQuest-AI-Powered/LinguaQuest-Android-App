package com.iti.linguaquest.features.lockscreen.presentation.view.component
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ImageWrapper
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord

@Composable
fun LargeWordCard(
    word: LockScreenWord,
    onGotItClick: () -> Unit,
    onWordClick: () -> Unit,
    onSpeakClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp, bottom = 24.dp), // Space for floating image
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp) // Push content down to avoid floating image
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val displayLevel = when (word.proficiencyLevel.trim().lowercase()) {
                        "beginner", "easy", "سهل", "مبتدئ" -> stringResource(R.string.easy)
                        "intermediate", "medium", "متوسط" -> stringResource(R.string.medium)
                        "advanced", "hard", "صعب", "متقدم" -> stringResource(R.string.hard)
                        else -> word.proficiencyLevel
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(AppColors.OrangeActive.copy(alpha = 0.15f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = displayLevel,
                            color = AppColors.OrangeActive,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = onSpeakClick,
                        modifier = Modifier
                            .size(36.dp)
                            .background(AppColors.OrangeActive.copy(alpha = 0.15f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
                            contentDescription = null,
                            tint = AppColors.OrangeActive,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = word.word,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF6B4226), // Dark brown color like screenshot
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable { onWordClick() }
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = word.translation,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF8D6E63),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (word.exampleSentence.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = AppColors.OrangeActive.copy(alpha = 0.08f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.lockscreen_example_usage_label),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = LinguaQuestTheme.colors.titleAndCationsColor.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = word.exampleSentence,
                                style = MaterialTheme.typography.bodyMedium,
                                color = LinguaQuestTheme.colors.titleAndCationsColor,
                                lineHeight = 22.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                AppButton3D(
                    text = stringResource(R.string.lockscreen_got_it),
                    onClick = onGotItClick,
                    buttonHeight = 52.dp
                )
            }
        }

        ImageWrapper(
            model = R.drawable.lingo_app_bar,
            contentDescription = null,
            modifier = Modifier
                .size(140.dp)
                .offset(y = (-30).dp)
        )
    }
}
