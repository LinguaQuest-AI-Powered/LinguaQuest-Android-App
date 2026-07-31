package com.iti.linguaquest.features.help.presentation.help.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors

@Composable
  fun HelpActionCard(
    onContactSupportClick: () -> Unit,
    onReportBugClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalLinguaQuestColors.current

    Card(
        modifier = modifier
            .padding(horizontal = 2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.whiteColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            HelpActionRow(
                title = stringResource(id = R.string.help_contact_support_action),
                leadingIcon = Icons.Outlined.ChatBubbleOutline,
                iconBackgroundColor = colors.IconBoxBackground,
                onClick = onContactSupportClick
            )

            Divider(
                color = colors.ProfileCardBorderColor.copy(alpha = 0.55f),
                thickness = 1.dp
            )

            HelpActionRow(
                title = stringResource(id = R.string.help_report_bug_action),
                leadingIcon = Icons.Outlined.BugReport,
                iconBackgroundColor = colors.ErrorAccent.copy(alpha = 0.12f),
                iconTint = colors.ErrorAccent,
                onClick = onReportBugClick
            )
        }
    }
}
