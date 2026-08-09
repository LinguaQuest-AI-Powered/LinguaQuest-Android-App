package com.iti.linguaquest.features.game.presentation.camera.view.component

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun CameraTopBar(
    modifier: Modifier = Modifier,
    targetWord: String,
    onBackClicked: () -> Unit,
    sideOptions: @Composable () -> Unit = {},
) {
    LinguaQuestScreenTopBar(
        title = null,
        onBackClicked = onBackClicked,
        modifier = modifier,
        showDivider = false,
        contentPadding = PaddingValues(0.dp),
        backButtonSize = 44.dp,
        backButtonBackgroundColor = LinguaQuestTheme.colors.blackColor.copy(alpha = 0.5f),
        backButtonContentColor = LinguaQuestTheme.colors.whiteColor,
        backButtonIconSize = 24.dp,
        centerContent = {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LinguaQuestTheme.colors.blackColor.copy(alpha = 0.6f),
                    contentColor = LinguaQuestTheme.colors.whiteColor
                ),
                modifier = Modifier.align(Alignment.Center)
            ) {
                androidx.compose.foundation.layout.Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_icon),
                        tint = LinguaQuestTheme.colors.whiteColor
                    )
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(R.string.find_label) + "\n")
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = MaterialTheme.typography.headlineSmall.fontSize
                                )
                            ) {
                                append(targetWord)
                            }
                        },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        color = LinguaQuestTheme.colors.whiteColor
                    )
                }
            }
        },
        trailingContent = {
            sideOptions()
        }
    )
}
