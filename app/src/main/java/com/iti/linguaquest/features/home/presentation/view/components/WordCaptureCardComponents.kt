package com.iti.linguaquest.features.home.presentation.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
internal fun CameraAccessories() {
    val knobCoral = LinguaQuestTheme.colors.CameraKnobCoral
    val knobCoralHighlight = LinguaQuestTheme.colors.CameraKnobCoralHighlight

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 28.dp)
                .offset(y = (-18).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(width = 44.dp, height = 26.dp)
                    .background(
                        color = knobCoral,
                        shape = RoundedCornerShape(
                            topStart = 22.dp,
                            topEnd = 22.dp,
                            bottomStart = 6.dp,
                            bottomEnd = 6.dp
                        )
                    )
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 4.dp)
                        .size(width = 28.dp, height = 5.dp)
                        .background(knobCoralHighlight, RoundedCornerShape(50))
                )
            }
            Box(
                modifier = Modifier
                    .size(width = 48.dp, height = 10.dp)
                    .background(LinguaQuestTheme.colors.Espresso, RoundedCornerShape(4.dp))
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 26.dp)
                .offset(y = (-17).dp)
                .size(width = 80.dp, height = 40.dp)
                .background(
                    color = LinguaQuestTheme.colors.ProfileCardColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(7.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(LinguaQuestTheme.colors.Charcoal)
        )
    }
}

@Composable
internal fun CameraInfoLeft(
    modifier: Modifier = Modifier,
    questLabel: String,
    worldName: String,
    instruction: String
) {
    Column(modifier = modifier) {
        Text(
            text = questLabel,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = worldName,
            style = MaterialTheme.typography.titleLarge.copy(
                color = LinguaQuestTheme.colors.blackColor,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = instruction,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = LinguaQuestTheme.colors.titleAndCationsColor
            )
        )

        Spacer(modifier = Modifier.height(0.dp))

        Image(
            painter = painterResource(id = R.drawable.lingo_camera),
            contentDescription = stringResource(R.string.mascot_camera_cd),
            modifier = Modifier
                .height(120.dp)
                .offset(x = (-8).dp, y = 4.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
internal fun CameraInfoRight(
    progressText: String,
    targetWord: String
) {
    Column(horizontalAlignment = Alignment.End) {
        Box(
            modifier = Modifier
                .background(
                    color = LinguaQuestTheme.colors.whiteColor,
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = LinguaQuestTheme.colors.ProfileCardBorderColor,
                    shape = CircleShape
                )
                .padding(horizontal = 12.dp, vertical = 5.dp)
        ) {
            Text(
                text = progressText,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = LinguaQuestTheme.colors.BrownText,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(2.dp))
        CameraLens(targetWord = targetWord)
    }
}

@Composable
internal fun CameraLens(targetWord: String) {
    val colors = LinguaQuestTheme.colors
    val tertiary = MaterialTheme.colorScheme.tertiary
    val tertiaryLight = lerp(tertiary, colors.whiteColor, 0.3f)
    val tertiaryDark = lerp(tertiary, colors.blackColor, 0.2f)
    val ringLight = lerp(colors.Sand, colors.whiteColor, 0.45f)
    val ringDark = lerp(colors.Sand, colors.blackColor, 0.08f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(168.dp)
            .offset(x = (-12).dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    ambientColor = colors.blackColor.copy(alpha = 0.16f),
                    spotColor = colors.blackColor.copy(alpha = 0.27f)
                )
                .background(colors.whiteColor, CircleShape)
                .border(1.dp, colors.ProfileCardBorderColor.copy(alpha = 0.6f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(
                    brush = Brush.linearGradient(listOf(tertiaryLight, tertiary, tertiaryDark)),
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(122.dp)
                .background(
                    brush = Brush.linearGradient(listOf(ringLight, colors.Sand, ringDark)),
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(98.dp)
                .background(colors.ProfileCardColor, CircleShape)
                .border(1.5.dp, colors.Espresso.copy(alpha = 0.15f), CircleShape)
        )
        Text(
            text = targetWord,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
