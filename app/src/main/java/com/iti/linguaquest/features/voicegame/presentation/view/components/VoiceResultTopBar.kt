package com.iti.linguaquest.features.voicegame.presentation.view.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun VoiceResultTopBar(
    coins: Int,
    showCoins: Boolean,
    onCoinPillPositioned: (Offset) -> Unit
) {
    LinguaQuestScreenTopBar(
        title = null,
        onBackClicked = {},
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.Transparent,
        showDivider = false,
        showBackButton = false,
        contentPadding = PaddingValues(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 12.dp),
        trailingContent = {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(LinguaQuestTheme.colors.whiteColor)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .onGloballyPositioned { cords ->
                        val center = cords.positionInRoot() + Offset(
                            cords.size.width / 2f,
                            cords.size.height / 2f
                        )
                        onCoinPillPositioned(center)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_coin),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                if (showCoins) {
                    Text(
                        "$coins",
                        fontWeight = FontWeight.Bold,
                        color = LinguaQuestTheme.colors.iconsColor,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    )
}
