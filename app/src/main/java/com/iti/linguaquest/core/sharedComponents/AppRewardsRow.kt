package com.iti.linguaquest.core.sharedComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun AppRewardsRow(
    coinsAmount: Int,
    xpAmount: Int,
    modifier: Modifier = Modifier,
    xpLabel: String = stringResource(R.string.xp),
    coinsLabel: String = stringResource(R.string.coins_label),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(16.dp)
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement
    ) {
        if (xpAmount > 0) {
            AppRewardPill(
                iconRes = R.drawable.ic_xp,
                amount = "+$xpAmount",
                label = xpLabel,
                modifier = Modifier.weight(1f)
            )
        }
        if (coinsAmount > 0) {
            AppRewardPill(
                iconRes = R.drawable.ic_coin,
                amount = "+$coinsAmount",
                label = coinsLabel,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AppRewardPill(
    iconRes: Int,
    amount: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(32.dp),
                ambientColor = LinguaQuestTheme.colors.iconsColor.copy(alpha = 0.3f),
                spotColor = LinguaQuestTheme.colors.iconsColor.copy(alpha = 0.3f)
            )
            .background(
                color = LinguaQuestTheme.colors.whiteColor,
                shape = RoundedCornerShape(32.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = amount,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LinguaQuestTheme.colors.BrownText
            )
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LinguaQuestTheme.colors.iconsColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppRewardsRowPreview() {
    LinguaQuestTheme {
        AppRewardsRow(
            coinsAmount = 50,
            xpAmount = 150
        )
    }
}
