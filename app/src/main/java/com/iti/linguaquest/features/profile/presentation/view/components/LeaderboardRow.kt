package com.iti.linguaquest.features.profile.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.Card3DWrapper
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.profile.presentation.model.LeaderboardEntry

@Composable
fun LeaderboardRow(
    entry: LeaderboardEntry,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    ledgeHeight: Dp = if (entry.isCurrentUser) 6.dp else 4.dp,
    cornerRadius: Dp = 16.dp
) {
    val borderColor = if (entry.isCurrentUser)
        MaterialTheme.colorScheme.tertiary
    else
        LinguaQuestTheme.colors.Sand

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = if (entry.isCurrentUser) 8.dp else 0.dp)
    ) {
        Card3DWrapper(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = LinguaQuestTheme.colors.whiteColor,
            borderColor = borderColor,
            borderWidth = if (entry.isCurrentUser) 1.5.dp else 1.dp,
            onClick = onClick,
            ledgeHeight = ledgeHeight,
            cornerRadius = cornerRadius
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.rank.toString(),
                    color = if (entry.isCurrentUser)
                        MaterialTheme.colorScheme.tertiary
                    else
                        LinguaQuestTheme.colors.iconsColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(28.dp)
                )

                AsyncImage(
                    model = entry.avatarUrl,
                    contentDescription = entry.name,
                    modifier = Modifier
                        .size(38.dp)
                        .then(
                            if (entry.isCurrentUser) {
                                Modifier
                                    .border(2.dp, MaterialTheme.colorScheme.tertiary, CircleShape)
                                    .padding(3.dp)
                                    .clip(CircleShape)
                            } else {
                                Modifier
                                    .border(
                                        1.5.dp,
                                        LinguaQuestTheme.colors.ProfileCardBorderColor,
                                        CircleShape
                                    )
                                    .padding(2.5.dp)
                                    .clip(CircleShape)
                            }
                        )
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = entry.name,
                        color = if (entry.isCurrentUser)
                            MaterialTheme.colorScheme.tertiary
                        else
                            LinguaQuestTheme.colors.blackColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Text(
                        text = entry.title,
                        color = if (entry.isCurrentUser)
                            MaterialTheme.colorScheme.tertiary
                        else
                            LinguaQuestTheme.colors.iconsColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "${entry.xp}",
                        color = if (entry.isCurrentUser)
                            MaterialTheme.colorScheme.tertiary
                        else
                            LinguaQuestTheme.colors.blackColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Text(
                        text = stringResource(R.string.xp),
                        color = if (entry.isCurrentUser)
                            MaterialTheme.colorScheme.tertiary
                        else
                            LinguaQuestTheme.colors.iconsColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (entry.isCurrentUser) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-16).dp, y = (-8).dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = stringResource(R.string.you_label),
                    color = LinguaQuestTheme.colors.whiteColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}