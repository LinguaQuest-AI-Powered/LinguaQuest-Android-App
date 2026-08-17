package com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.IconPosition

@Composable
fun ClaimRewardButton(
    modifier: Modifier = Modifier,
    isClaiming: Boolean = false,
    onClaimClick: () -> Unit
) {
    AppButton3D(
        text = stringResource(id = R.string.claim_reward),
        onClick = onClaimClick,
        modifier = modifier,
        variant = ButtonVariant.PRIMARY,
        icon = rememberVectorPainter(Icons.Outlined.CardGiftcard),
        iconPosition = IconPosition.END,
        tintIcon = true,
        enabled = !isClaiming,
        isLoading = isClaiming
    )
}
