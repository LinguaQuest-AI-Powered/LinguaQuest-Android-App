package com.iti.linguaquest.core.sharedComponents.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.AppGradientBackgroundBox
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.IconPosition
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun AppDialog(
    title: String,
    message: String,
    imageRes: Int? = null,
    onDismissRequest: () -> Unit = {},
    showCloseIcon: Boolean = false,
    primaryButtonText: String,
    isPrimaryButtonEnabled: Boolean = true,
    onPrimaryClick: () -> Unit,
    primaryButtonIcon: Int? = null,
    secondaryButtonText: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
    secondaryButtonIcon: Int? = null,
    gradientColors: List<Color> = listOf(LinguaQuestTheme.colors.DialogGradientTopRight, Color.White, LinguaQuestTheme.colors.DialogGradientBottomLeft),
    outlineColor: Color = AppColors.DialogOutline,
    secondaryButtonOutlineColor: Color = AppColors.DialogSecondaryButtonOutline,
    customContent: (@Composable () -> Unit)? = null
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AppDialogContent(
            title = title,
            message = message,
            imageRes = imageRes,
            onDismissRequest = onDismissRequest,
            showCloseIcon = showCloseIcon,
            primaryButtonText = primaryButtonText,
            isPrimaryButtonEnabled = isPrimaryButtonEnabled,
            onPrimaryClick = onPrimaryClick,
            primaryButtonIcon = primaryButtonIcon,
            secondaryButtonText = secondaryButtonText,
            onSecondaryClick = onSecondaryClick,
            secondaryButtonIcon = secondaryButtonIcon,
            gradientColors = gradientColors,
            outlineColor = outlineColor,
            secondaryButtonOutlineColor = secondaryButtonOutlineColor,
            customContent = customContent
        )
    }
}

@Composable
fun AppDialogContent(
    title: String,
    message: String,
    imageRes: Int? = null,
    onDismissRequest: () -> Unit = {},
    showCloseIcon: Boolean = false,
    primaryButtonText: String,
    isPrimaryButtonEnabled: Boolean = true,
    onPrimaryClick: () -> Unit,
    primaryButtonIcon: Int? = null,
    secondaryButtonText: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
    secondaryButtonIcon: Int? = null,
    gradientColors: List<Color> = listOf(LinguaQuestTheme.colors.DialogGradientTopRight, Color.White, LinguaQuestTheme.colors.DialogGradientBottomLeft),
    outlineColor: Color = AppColors.DialogOutline,
    secondaryButtonOutlineColor: Color = AppColors.DialogSecondaryButtonOutline,
    customContent: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        AppGradientBackgroundBox(
            gradientColors = gradientColors,
            outlineColor = outlineColor,
            modifier = Modifier
                .padding(top = 48.dp)
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = AppColors.DialogShadow)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = AppTextStyles.DialogTitle.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = message,
                    style = AppTextStyles.DialogMessage.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    ),
                    textAlign = TextAlign.Center
                )

                if (customContent != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    customContent()
                    Spacer(modifier = Modifier.height(24.dp))
                } else {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                AppButton3D(
                    text = primaryButtonText,
                    onClick = onPrimaryClick,
                    variant = ButtonVariant.PRIMARY,
                    icon = primaryButtonIcon?.let { painterResource(it) },
                    iconPosition = if (primaryButtonIcon != null) IconPosition.START else IconPosition.NONE,
                    tintIcon = false,
                    enabled = isPrimaryButtonEnabled
                )

                if (secondaryButtonText != null && onSecondaryClick != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    AppButton3D(
                        text = secondaryButtonText,
                        onClick = onSecondaryClick,
                        variant = ButtonVariant.SOCIAL,
                        icon = secondaryButtonIcon?.let { painterResource(it) },
                        iconPosition = if (secondaryButtonIcon != null) IconPosition.START else IconPosition.NONE,
                        contentColorOverride = secondaryButtonOutlineColor,
                        borderColorOverride = secondaryButtonOutlineColor,
                        backgroundColorOverride = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }

        if (imageRes != null) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(110.dp)
                    .offset(y = (-10).dp)
            )
        }

        // Close Button
        if (showCloseIcon) {
            Box(
                modifier = Modifier
                    .padding(top = 64.dp, end = 16.dp)
                    .align(Alignment.TopEnd)
                    .size(28.dp)
                    .background(AppColors.DialogCloseButtonBackground, CircleShape)
                    .clip(CircleShape)
                    .clickable { onDismissRequest() },
                contentAlignment = Alignment.Center
            ) {
                Text("✕", color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun LightTipContent() {
    Row(
        modifier = Modifier
            .background(AppColors.DialogLightTipBackground, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Bulb icon placeholder
        Text("💡 ", fontSize = 16.sp)
        Text(
            text = "MAKE SURE IT'S WELL LIT!",
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

@Composable
fun PriceTagContent(price: Int) {
    Row(
        modifier = Modifier
            .background(AppColors.DialogPriceTagBackground, RoundedCornerShape(24.dp))
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_coin),
            contentDescription = null,
            modifier = Modifier.size(24.dp).padding(end = 4.dp)
        )
        Text(
            text = "$price",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF333333)
@Composable
fun PreviewNotQuiteDialog() {
    LinguaQuestTheme {
        AppDialogContent(
            title = "Not quite!",
            message = "Lingo didn't see any bread there.\nTry framing it differently!",
            imageRes = R.drawable.lingo, 
            primaryButtonText = "Retry Camera",
            onPrimaryClick = {},
            secondaryButtonText = "Change Word",
            onSecondaryClick = {},
            customContent = { LightTipContent() }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF333333)
@Composable
fun PreviewNotEnoughCoinsDialog() {
    LinguaQuestTheme {
        AppDialogContent(
            title = "Not enough coins!",
            message = "You need more coins to use\nthis hint. Keep exploring to\nearn more!",
            imageRes = R.drawable.lingo_error,
            primaryButtonText = "Get More Coins",
            onPrimaryClick = {},
            showCloseIcon = true
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF333333)
@Composable
fun PreviewStuckOnThisOneDialog() {
    LinguaQuestTheme {
        AppDialogContent(
            title = "Stuck on this one?",
            message = "Skip this word for 200 coins?",
            imageRes = R.drawable.lingo,
            primaryButtonText = "Skip Word",
            onPrimaryClick = {},
            secondaryButtonText = "Cancel",
            onSecondaryClick = {},
            secondaryButtonOutlineColor = MaterialTheme.colorScheme.tertiary,
            customContent = { PriceTagContent(-200) }
        )
    }
}
