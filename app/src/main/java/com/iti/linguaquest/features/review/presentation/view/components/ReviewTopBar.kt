package com.iti.linguaquest.features.review.presentation.view.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBarBackButtonStyle
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ReviewTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBack: () -> Unit,
) {
    LinguaQuestScreenTopBar(
        title = title,
        onBackClicked = onBack,
        modifier = modifier
    )
}


@Preview(showBackground = true, name = "ReviewTopBar")
@Composable
private fun ReviewTopBarPreview() {
    LinguaQuestTheme {
        ReviewTopBar(
            title = "AI Review",
            onBack = {}
        )
    }
}
