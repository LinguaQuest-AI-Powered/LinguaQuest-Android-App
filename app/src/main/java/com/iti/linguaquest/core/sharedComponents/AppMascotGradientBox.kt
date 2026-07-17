package com.iti.linguaquest.core.sharedComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppMascotGradientBox(
    imageRes: Int,
    modifier: Modifier = Modifier,
    mascotOverlapHeight: Dp = 70.dp,
    mascotSize: Dp = 160.dp,
    onMascotClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        AppGradientBackgroundBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = mascotOverlapHeight)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = mascotOverlapHeight + 8.dp)
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }

        val baseImageModifier = Modifier
            .offset(y = (-10).dp)
            .size(mascotSize)
        val finalImageModifier = if (onMascotClick != null) {
            baseImageModifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onMascotClick
            )
        } else {
            baseImageModifier
        }

        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Mascot",
            modifier = finalImageModifier
        )
    }
}