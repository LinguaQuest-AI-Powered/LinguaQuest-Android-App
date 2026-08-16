package com.iti.linguaquest.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.tutorial.presentation.tutorialTarget

import androidx.compose.material3.MaterialTheme

@Composable
fun GameBottomNavBar(
    items: List<BottomNavScreen>,
    currentRoute: Any?,
    onItemClick: (BottomNavScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val barShape = RoundedCornerShape(36.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 10.dp,
                shape = barShape,
                spotColor = LinguaQuestTheme.colors.blackColor.copy(alpha = 0.15f),
                ambientColor = LinguaQuestTheme.colors.blackColor.copy(alpha = 0.06f)
            )
            .clip(barShape)
            .background(LinguaQuestTheme.colors.ProfileCardColor)
            .border(
                width = 1.dp,
                color = LinguaQuestTheme.colors.ProfileCardBorderColor.copy(alpha = 0.6f),
                shape = barShape
            )
            .padding(horizontal = 6.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { screen ->
            val isSelected = currentRoute == screen.route
            val targetId = when (screen) {
                BottomNavScreen.Home -> "bottom_nav_home"
                BottomNavScreen.Gallery -> "bottom_nav_gallery"
                BottomNavScreen.Lingos -> "bottom_nav_lingos"
                BottomNavScreen.Profile -> "bottom_nav_profile"
            }

            val activeColor = MaterialTheme.colorScheme.tertiary
            val inactiveColor = LinguaQuestTheme.colors.iconsColor
            val activeBackground = if (LinguaQuestTheme.colors.isDark) {
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.22f)
            } else {
                LinguaQuestTheme.colors.DialogGradientTopRight
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(26.dp))
                    .background(if (isSelected) activeBackground else Color.Transparent)
                    .clickable { onItemClick(screen) }
                    .tutorialTarget(targetId)
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                ItemContent(
                    screen = screen,
                    color = if (isSelected) activeColor else inactiveColor,
                    isSelected = isSelected
                )
            }
        }
    }
}

@Composable
private fun ItemContent(screen: BottomNavScreen, color: Color, isSelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = screen.icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = stringResource(id = screen.labelRes),
            color = color,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
