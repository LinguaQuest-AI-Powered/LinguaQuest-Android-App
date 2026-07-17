package com.iti.linguaquest.features.home.presentation.view.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.IconPosition
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun ContinueLessonCard(
    lesson: LessonPreview,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(LinguaQuestTheme.colors.ChipBackground)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.continue_lesson),
                        color = LinguaQuestTheme.colors.iconsColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = lesson.word,
                    color = LinguaQuestTheme.colors.blackColor,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${lesson.partOfSpeech} \u2022 ${lesson.translation}",
                    color = LinguaQuestTheme.colors.iconsColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .offset(y = (-12).dp)
                    .size(76.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(LinguaQuestTheme.colors.IconBoxBackground),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = lesson.iconRes),
                    contentDescription = lesson.word,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        AppButton(
            text = stringResource(R.string.continue_button),
            onClick = onContinueClick,
            variant = ButtonVariant.PRIMARY,
            icon = rememberVectorPainter(image = Icons.Default.PlayArrow),
            iconPosition = IconPosition.START,
            modifier = Modifier.fillMaxWidth()
        )
    }
}