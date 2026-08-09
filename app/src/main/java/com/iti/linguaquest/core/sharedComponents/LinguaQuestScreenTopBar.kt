package com.iti.linguaquest.core.sharedComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.core.utils.formatCompact

enum class LinguaQuestScreenTopBarBackButtonStyle {
    IconButton,
    Circular
}

@Composable
fun LinguaQuestScreenTopBar(
    title: String? = null,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isTitleCentered: Boolean? = null,
    containerColor: Color = Color.Unspecified,
    titleColor: Color = Color.Unspecified,
    titleTextStyle: TextStyle? = null,
    showDivider: Boolean? = null,
    dividerColor: Color = Color.Unspecified,
    dividerThickness: Dp = 1.dp,
    dividerSpacing: Dp? = null,
    showCoins: Boolean = false,
    coinsCount: Int = 0,
    showXp: Boolean = false,
    xpCount: Int = 0,
    showBackButton: Boolean = true,
    startContent: (@Composable RowScope.() -> Unit)? = null,
    centerContent: (@Composable BoxScope.() -> Unit)? = null,
    trailingContent: @Composable RowScope.() -> Unit = {},
    applyStatusBarsPadding: Boolean? = null,
    contentPadding: PaddingValues? = null,
    backButtonStyle: LinguaQuestScreenTopBarBackButtonStyle? = null,
    backButtonSize: Dp = 44.dp,
    backButtonBackgroundColor: Color = Color.Unspecified,
    backButtonContentColor: Color = Color.Unspecified,
    backButtonIconSize: Dp = 24.dp
) {
    val colors = LocalLinguaQuestColors.current
    val effectiveTitleCentered = isTitleCentered ?: true
    val effectiveContainerColor = if (containerColor == Color.Unspecified) Color.Transparent else containerColor
    val effectiveTitleColor = if (titleColor == Color.Unspecified) colors.BrownText else titleColor
    val effectiveTitleTextStyle = titleTextStyle
        ?: MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    val effectiveShowDivider = showDivider ?: true
    val effectiveDividerColor = if (dividerColor == Color.Unspecified) LinguaQuestTheme.colors.ProfileCardBorderColor else dividerColor
    val effectiveDividerSpacing = dividerSpacing ?: 16.dp
    val effectiveApplyStatusBarsPadding = applyStatusBarsPadding ?: true
    val effectiveContentPadding = contentPadding ?: PaddingValues(horizontal = 22.dp, vertical = 0.dp)
    val effectiveBackButtonStyle = backButtonStyle ?: LinguaQuestScreenTopBarBackButtonStyle.Circular
    val effectiveBackButtonBackgroundColor = if (backButtonBackgroundColor == Color.Unspecified) colors.whiteColor else backButtonBackgroundColor
    val effectiveBackButtonContentColor = if (backButtonContentColor == Color.Unspecified) colors.OrangeActive else backButtonContentColor

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(effectiveContainerColor)
    ) {
        val barModifier = Modifier
            .fillMaxWidth()
            .background(effectiveContainerColor)

        val paddedModifier = if (effectiveApplyStatusBarsPadding) {
            barModifier.statusBarsPadding().padding(effectiveContentPadding)
        } else {
            barModifier.padding(effectiveContentPadding)
        }

        if (effectiveTitleCentered) {
            Box(
                modifier = paddedModifier,
                contentAlignment = Alignment.Center
            ) {
                if (showBackButton || startContent != null) {
                    Row(
                        modifier = Modifier.align(Alignment.CenterStart),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (showBackButton) {
                            BackButton(
                                onBackClicked = onBackClicked,
                                style = effectiveBackButtonStyle,
                                size = backButtonSize,
                                backgroundColor = effectiveBackButtonBackgroundColor,
                                contentColor = effectiveBackButtonContentColor,
                                iconSize = backButtonIconSize
                            )
                        }

                        if (startContent != null) {
                            if (showBackButton) {
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            startContent()
                        }
                    }
                }

                if (centerContent != null) {
                    Box(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        centerContent()
                    }
                } else if (title != null) {
                    Text(
                        text = title,
                        style = effectiveTitleTextStyle,
                        color = effectiveTitleColor,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                EndContent(
                    showXp = showXp,
                    xpCount = xpCount,
                    showCoins = showCoins,
                    coinsCount = coinsCount,
                    trailingContent = trailingContent,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        } else {
            Row(
                modifier = paddedModifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (showBackButton) {
                    BackButton(
                        onBackClicked = onBackClicked,
                        style = effectiveBackButtonStyle,
                        size = backButtonSize,
                        backgroundColor = effectiveBackButtonBackgroundColor,
                        contentColor = effectiveBackButtonContentColor,
                        iconSize = backButtonIconSize
                    )
                }

                if (startContent != null) {
                    startContent()
                }

                if (title != null) {
                    Text(
                        text = title,
                        style = effectiveTitleTextStyle,
                        color = effectiveTitleColor,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                EndContent(
                    showXp = showXp,
                    xpCount = xpCount,
                    showCoins = showCoins,
                    coinsCount = coinsCount,
                    trailingContent = trailingContent
                )
            }
        }

        if (effectiveShowDivider) {
            Spacer(modifier = Modifier.height(effectiveDividerSpacing))
            HorizontalDivider(
                color = effectiveDividerColor,
                thickness = dividerThickness
            )
        }
    }
}

@Composable
private fun BackButton(
    onBackClicked: () -> Unit,
    style: LinguaQuestScreenTopBarBackButtonStyle,
    size: Dp,
    backgroundColor: Color,
    contentColor: Color,
    iconSize: Dp,
    modifier: Modifier = Modifier
) {
    when (style) {
        LinguaQuestScreenTopBarBackButtonStyle.IconButton -> {
            IconButton(
                onClick = onBackClicked,
                modifier = modifier.size(size)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = stringResource(id = R.string.back),
                    tint = contentColor,
                    modifier = Modifier.size(iconSize)
                )
            }
        }

        LinguaQuestScreenTopBarBackButtonStyle.Circular -> {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .clickable(onClick = onBackClicked),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = stringResource(id = R.string.back),
                    tint = contentColor,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}

@Composable
private fun EndContent(
    showXp: Boolean,
    xpCount: Int,
    showCoins: Boolean,
    coinsCount: Int,
    trailingContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showXp) {
            StatChip(
                iconRes = R.drawable.ic_start,
                value = xpCount,
                textColor = LinguaQuestTheme.colors.iconsColor
            )
        }

        if (showXp && showCoins) {
            Spacer(modifier = Modifier.width(6.dp))
        }

        if (showCoins) {
            StatChip(
                iconRes = R.drawable.ic_coin,
                value = coinsCount,
                textColor = LinguaQuestTheme.colors.iconsColor
            )
        }

        if (showXp || showCoins) {
            Spacer(modifier = Modifier.width(6.dp))
        }

        trailingContent()
    }
}

@Composable
private fun StatChip(
    iconRes: Int,
    value: Int,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(50),
                spotColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = value.formatCompact(),
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFF8F2)
@Composable
private fun LinguaQuestScreenTopBarPreview() {
    LinguaQuestTheme {
        LinguaQuestScreenTopBar(
            title = "Settings",
            onBackClicked = {},
            showDivider = true
        )
    }
}
