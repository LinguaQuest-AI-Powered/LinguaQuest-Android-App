package com.iti.linguaquest.core.sharedComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.core.utils.formatCompact

@Composable
fun LinguaQuestScreenTopBar(
    title: String? = null,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isTitleCentered: Boolean = true,
    containerColor: Color = Color.Transparent,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    showDivider: Boolean = false,
    showCoins: Boolean = false,
    coinsCount: Int = 0,
    showXp: Boolean = false,
    xpCount: Int = 0,
    trailingContent: @Composable RowScope.() -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(containerColor)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClicked
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = titleColor,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showXp) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(LocalLinguaQuestColors.current.whiteColor)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_start),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = xpCount.formatCompact(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = LinguaQuestTheme.colors.iconsColor
                        )
                    }
                }

                if (showCoins && showXp) {
                    Spacer(Modifier.width(6.dp))
                }

                if (showCoins) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(LocalLinguaQuestColors.current.whiteColor)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_coin),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = coinsCount.formatCompact(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = LinguaQuestTheme.colors.iconsColor
                        )
                    }
                }

                trailingContent()
            }
        }

        if (showDivider) {
            HorizontalDivider(
                color = LinguaQuestTheme.colors.ProfileCardBorderColor,
                thickness = 1.dp
            )
        }
    }
}
