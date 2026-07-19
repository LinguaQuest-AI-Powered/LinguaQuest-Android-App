package com.iti.linguaquest.features.profile.presentation.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.profile.presentation.model.LeaderboardEntry

@Composable
fun LeaderboardRow(entry: LeaderboardEntry) {

    val borderColor = if (entry.isCurrentUser)
        MaterialTheme.colorScheme.tertiary
    else
        Color(0xFFDAC2AE)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(bottom = if (entry.isCurrentUser) 6.dp else 4.dp)
            .background(
                color = LinguaQuestTheme.colors.whiteColor,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = if (entry.isCurrentUser) 1.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
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
                    .size(36.dp)
                    .clip(CircleShape)
                    .let {
                        if (entry.isCurrentUser)
                            it.border(
                                2.dp,
                                MaterialTheme.colorScheme.tertiary,
                                CircleShape
                            )
                        else
                            it
                    }
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
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

                    if (entry.isCurrentUser) {

                        Spacer(modifier = Modifier.width(6.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(MaterialTheme.colorScheme.tertiary)
                                .padding(
                                    horizontal = 8.dp,
                                    vertical = 2.dp
                                )
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
                    text = "XP",
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
}
// fun LeaderboardRow(entry: LeaderboardEntry) {
//    Surface(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp),
//        color = LinguaQuestTheme.colors.whiteColor,
//        border = if (entry.isCurrentUser) BorderStroke(
//            1.5.dp,
//            MaterialTheme.colorScheme.tertiary
//        ) else BorderStroke(
//            1.dp,
//            Color(0xFFDAC2AE)
//        )
//    ) {
//        Row(
//            Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                entry.rank.toString(),
//                color = if (entry.isCurrentUser) MaterialTheme.colorScheme.tertiary else LinguaQuestTheme.colors.iconsColor,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.width(28.dp)
//            )
//            AsyncImage(
//                model = entry.avatarUrl,
//                contentDescription = entry.name,
//                modifier = Modifier
//                    .size(36.dp)
//                    .clip(CircleShape)
//                    .let {
//                        if (entry.isCurrentUser) it.border(
//                            2.dp,
//                            MaterialTheme.colorScheme.tertiary,
//                            CircleShape
//                        )
//                        else it
//                    }
//            )
//            Spacer(Modifier.width(10.dp))
//            Column(Modifier.weight(1f)) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text(
//                        entry.name,
//                        color = if (entry.isCurrentUser) MaterialTheme.colorScheme.tertiary
//                        else LinguaQuestTheme.colors.blackColor,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 14.sp
//                    )
//                    if (entry.isCurrentUser) {
//                        Spacer(Modifier.width(6.dp))
//                        Box(
//                            modifier = Modifier
//                                .clip(RoundedCornerShape(50))
//                                .background(MaterialTheme.colorScheme.tertiary)
//                                .padding(horizontal = 8.dp, vertical = 2.dp)
//                        ) {
//                            Text(
//                                stringResource(R.string.you_label),
//                                color = LinguaQuestTheme.colors.whiteColor,
//                                fontSize = 10.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                        }
//                    }
//                }
//                Text(entry.title, color = if (entry.isCurrentUser) MaterialTheme.colorScheme.tertiary
//                else LinguaQuestTheme.colors.iconsColor, fontSize = 12.sp,fontWeight = FontWeight.SemiBold)
//            }
//            Column(horizontalAlignment = Alignment.End) {
//                Text(
//                    "${entry.xp}",
//                    color = if (entry.isCurrentUser) MaterialTheme.colorScheme.tertiary
//                    else LinguaQuestTheme.colors.blackColor,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 14.sp
//                )
//                Text("XP", color = if (entry.isCurrentUser) MaterialTheme.colorScheme.tertiary
//                else LinguaQuestTheme.colors.iconsColor, fontSize = 10.sp,fontWeight = FontWeight.Bold)
//            }
//        }
//    }
//}