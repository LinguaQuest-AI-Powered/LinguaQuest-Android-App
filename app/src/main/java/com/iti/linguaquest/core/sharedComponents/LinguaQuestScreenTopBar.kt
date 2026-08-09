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
    isTitleCentered: Boolean = true,
    containerColor: Color = Color.Transparent,
    titleColor: Color = LocalLinguaQuestColors.current.BrownText,
    titleTextStyle: TextStyle = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    showDivider: Boolean = true,
    dividerColor: Color = LinguaQuestTheme.colors.ProfileCardBorderColor,
    dividerThickness: Dp = 1.dp,
    dividerSpacing: Dp = 16.dp,
    showCoins: Boolean = false,
    coinsCount: Int = 0,
    showXp: Boolean = false,
    xpCount: Int = 0,
    showBackButton: Boolean = true,
    startContent: (@Composable RowScope.() -> Unit)? = null,
    centerContent: (@Composable BoxScope.() -> Unit)? = null,
    trailingContent: @Composable RowScope.() -> Unit = {},
    applyStatusBarsPadding: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 22.dp, vertical = 0.dp),
    backButtonStyle: LinguaQuestScreenTopBarBackButtonStyle = LinguaQuestScreenTopBarBackButtonStyle.Circular,
    backButtonSize: Dp = 40.dp,
    backButtonBackgroundColor: Color = LocalLinguaQuestColors.current.whiteColor,
    backButtonContentColor: Color = LocalLinguaQuestColors.current.OrangeActive,
    backButtonIconSize: Dp = 18.dp
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor)
    ) {
        val barModifier = Modifier
            .fillMaxWidth()
            .background(containerColor)

        val paddedModifier = if (applyStatusBarsPadding) {
            barModifier.statusBarsPadding().padding(contentPadding)
        } else {
            barModifier.padding(contentPadding)
        }

        if (isTitleCentered) {
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
                                style = backButtonStyle,
                                size = backButtonSize,
                                backgroundColor = backButtonBackgroundColor,
                                contentColor = backButtonContentColor,
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
                        style = titleTextStyle,
                        color = titleColor,
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
                        style = backButtonStyle,
                        size = backButtonSize,
                        backgroundColor = backButtonBackgroundColor,
                        contentColor = backButtonContentColor,
                        iconSize = backButtonIconSize
                    )
                }

                if (startContent != null) {
                    startContent()
                }

                if (title != null) {
                    Text(
                        text = title,
                        style = titleTextStyle,
                        color = titleColor,
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

        if (showDivider) {
            Spacer(modifier = Modifier.height(dividerSpacing))
            HorizontalDivider(
                color = dividerColor,
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
