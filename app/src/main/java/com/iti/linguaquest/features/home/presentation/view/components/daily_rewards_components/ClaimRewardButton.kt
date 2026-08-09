package com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R


@Composable
fun ClaimRewardButton(isClaiming: Boolean = false, onClaimClick: () -> Unit) {
    Button(
        onClick = onClaimClick,
        enabled = !isClaiming,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(28.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.claim_reward),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = LinguaQuestTheme.colors.whiteColor
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            if (isClaiming) {
                LingoSpinningIcon(
                    size = 20.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.CardGiftcard,
                    contentDescription = stringResource(id = R.string.cd_gift),
                    tint = LinguaQuestTheme.colors.whiteColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
