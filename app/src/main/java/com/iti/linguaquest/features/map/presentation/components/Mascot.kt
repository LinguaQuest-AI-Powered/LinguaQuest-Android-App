package com.iti.linguaquest.features.map.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.MessageBubble

@Composable
fun Mascot(
    offsetX: Dp,
    offsetY: Dp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.absoluteOffset(x = offsetX, y = offsetY),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MessageBubble(
            title = stringResource(id = R.string.map_lets_learn)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Image(
            painter = painterResource(id = R.drawable.lingo_onboarding_1),
            contentDescription = stringResource(id = R.string.mascot_content_desc),
            modifier = Modifier.size(100.dp)
        )
    }
}

