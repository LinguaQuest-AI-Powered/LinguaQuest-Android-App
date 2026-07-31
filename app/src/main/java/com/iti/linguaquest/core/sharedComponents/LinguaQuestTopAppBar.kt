package com.iti.linguaquest.core.sharedComponents

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.formatCompact


@Composable
fun LinguaQuestTopAppBar(
    xp: Int,
    coins: Int,
    modifier: Modifier = Modifier
) {
    val animatedXp by animateIntAsState(
        targetValue = xp,
        animationSpec = tween(durationMillis = 600),
        label = "xpAnim"
    )
    val animatedCoins by animateIntAsState(
        targetValue = coins,
        animationSpec = tween(durationMillis = 600),
        label = "livesAnim"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                shape = RoundedCornerShape(0.dp)
            )
             .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
         Row(
             modifier = Modifier.weight(1f),
             verticalAlignment = Alignment.CenterVertically
         ) {

            Image(
                painter = painterResource(id = R.drawable.lingo_app_bar),
                contentDescription = stringResource(R.string.app_logo_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.background, CircleShape)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = CircleShape
                    )
                    .padding(3.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.3).sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

         Row(
            modifier = Modifier.padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatChip(
                iconRes = R.drawable.ic_start,
                value = animatedXp,
                textColor = MaterialTheme.colorScheme.onSurface
            )
            StatChip(
                iconRes = R.drawable.ic_coin,
                value = animatedCoins,
                textColor = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@Composable
private fun StatChip(
    iconRes: Int,
    value: Int,
    textColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(50), spotColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = value.formatCompact(),
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFF8F2)
@Composable
private fun LinguaQuestTopAppBarPreview() {
    LinguaQuestTheme {
        LinguaQuestTopAppBar(xp = 1250, coins = 45)
    }
}

