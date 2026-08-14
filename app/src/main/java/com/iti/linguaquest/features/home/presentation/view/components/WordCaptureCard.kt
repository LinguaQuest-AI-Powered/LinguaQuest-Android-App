package com.iti.linguaquest.features.home.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.IconPosition
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun WordCaptureCard(
    modifier: Modifier = Modifier,
    questLabel: String = stringResource(R.string.current_quest_label),
    worldName: String = stringResource(R.string.dummy_world_name),
    instruction: String = stringResource(R.string.find_and_capture_instruction),
    progressText: String = stringResource(R.string.dummy_progress_text),
    targetWord: String = stringResource(R.string.dummy_target_word),
    buttonText: String = stringResource(R.string.word_capture_continue_button),
    onContinueClick: (Rect) -> Unit = {}
) {
    val cardBounds = remember { arrayOf(Rect.Zero) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .onGloballyPositioned { coordinates ->
                cardBounds[0] = coordinates.boundsInRoot()
            }
    ) {
        CameraAccessories()

        CameraBody(
            questLabel = questLabel,
            worldName = worldName,
            instruction = instruction,
            progressText = progressText,
            targetWord = targetWord,
            buttonText = buttonText,
            onContinueClick = { onContinueClick(cardBounds[0]) }
        )
    }
}

@Composable
internal fun CameraBody(
    questLabel: String,
    worldName: String,
    instruction: String,
    progressText: String,
    targetWord: String,
    buttonText: String,
    onContinueClick: () -> Unit,
    ledgeHeight: Dp = 8.dp,
    cornerRadius: Dp = 26.dp
) {
    val colors = LinguaQuestTheme.colors

    val ledgeColor = colors.ProfileCardBorderColor.copy(alpha = 0.6f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = ledgeHeight)
    ) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = ledgeHeight)
                .background(
                    color = ledgeColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(cornerRadius),
                    ambientColor = colors.blackColor.copy(alpha = 0.12f),
                    spotColor = colors.blackColor.copy(alpha = 0.20f)
                )
                .background(
                    color = colors.whiteColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
                .border(
                    width = 2.dp,
                    color = colors.ProfileCardBorderColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.8f),
                                Color.White.copy(alpha = 0.0f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    CameraInfoLeft(
                        modifier = Modifier.weight(1f),
                        questLabel = questLabel,
                        worldName = worldName,
                        instruction = instruction
                    )

                    CameraInfoRight(
                        progressText = progressText,
                        targetWord = targetWord
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                AppButton3D(
                    text = buttonText,
                    onClick = onContinueClick,
                    icon = painterResource(id = R.drawable.ic_camera),
                    iconPosition = IconPosition.START,
                    backgroundColorOverride = MaterialTheme.colorScheme.primary,
                    ledgeColorOverride = LinguaQuestTheme.colors.ShadowOrange,
                    contentColorOverride = LinguaQuestTheme.colors.whiteColor
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun WordCaptureCardPreview() {
    LinguaQuestTheme {
        WordCaptureCard(
            modifier = Modifier.padding(16.dp),
            questLabel = "Current Quest",
            worldName = "Kitchen",
            instruction = "Find & capture:",
            progressText = "2 of 10",
            targetWord = "Apple",
            buttonText = "Continue"
        )
    }
}