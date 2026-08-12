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
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.iti.linguaquest.core.tutorial.domain.TutorialManager
import com.iti.linguaquest.core.tutorial.presentation.tutorialTarget

@Composable
fun GameBottomNavBar(
    items: List<BottomNavScreen>,
    currentRoute: Any?,
    onItemClick: (BottomNavScreen) -> Unit,
    modifier: Modifier = Modifier,
    tutorialManager: TutorialManager? = null
) {
    val barShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(barShape)
            .background(LinguaQuestTheme.colors.ProfileCardColor)
            .border(
                width = 1.dp,
                color = LinguaQuestTheme.colors.ProfileCardBorderColor,
                shape = barShape
            )
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { screen ->
            val isSelected = currentRoute == screen.route
            val targetId = when (screen) {
                BottomNavScreen.Home -> "bottom_nav_home"
                BottomNavScreen.Gallery -> "bottom_nav_gallery"
                BottomNavScreen.Profile -> "bottom_nav_profile"
                else -> null
            }
            val targetModifier = if (tutorialManager != null && targetId != null) {
                Modifier.tutorialTarget(targetId, tutorialManager)
            } else {
                Modifier
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .then(targetModifier)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onItemClick(screen) },
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(LinguaQuestTheme.colors.ShadowOrange, RoundedCornerShape(16.dp))
                            .padding(bottom = 4.dp)
                            .background(LinguaQuestTheme.colors.OrangeActive, RoundedCornerShape(16.dp))
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ItemContent(screen = screen, color = Color.Black, isSelected = true)
                    }
                } else {
                    ItemContent(screen = screen, color = LinguaQuestTheme.colors.BrownText, isSelected = false)
                }
            }
        }
    }
}

@Composable
private fun ItemContent(screen: BottomNavScreen, color: Color, isSelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = screen.icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = screen.labelRes),
            color = color,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
        )
    }
}
