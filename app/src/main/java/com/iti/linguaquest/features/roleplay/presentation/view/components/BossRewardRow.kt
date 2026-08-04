package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.game.presentation.result.view.component.RewardPill

@Composable
fun BossRewardRow(
    xpEarned: Int,
    coinsEarned: Int,
    modifier: Modifier = Modifier
) {
    val xpText = if (xpEarned > 0) "+$xpEarned" else "+150"
    val coinsText = if (coinsEarned > 0) "+$coinsEarned" else "+50"

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RewardPill(
            iconRes = R.drawable.ic_xp,
            amount = xpText,
            label = stringResource(R.string.roleplay_xp),
            modifier = Modifier.weight(1f)
        )
        RewardPill(
            iconRes = R.drawable.ic_coin,
            amount = coinsText,
            label = stringResource(R.string.roleplay_coins),
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BossRewardRowPreview() {
    LinguaQuestTheme {
        BossRewardRow(
            xpEarned = 200,
            coinsEarned = 75
        )
    }
}
