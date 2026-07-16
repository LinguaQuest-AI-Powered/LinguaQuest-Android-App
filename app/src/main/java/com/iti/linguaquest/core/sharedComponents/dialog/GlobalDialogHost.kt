package com.iti.linguaquest.core.sharedComponents.dialog


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun GlobalDialogHost(dialogController: DialogController) {
    val dialog by dialogController.state.collectAsStateWithLifecycle()

    dialog?.let { d ->
        AppDialog(
            title = d.title.asString(),
            message = d.message.asString(),
            imageRes = d.imageRes,
            onDismissRequest = { d.onDismiss?.invoke(); dialogController.hide() },
            showCloseIcon = d.showCloseIcon,
            primaryButtonText = d.confirmText.asString(),
            onPrimaryClick = { d.onConfirm?.invoke(); dialogController.hide() },
            primaryButtonIcon = d.primaryIconRes,
            secondaryButtonText = d.dismissText?.asString(),
            onSecondaryClick = if (d.dismissText != null) { { d.onDismiss?.invoke(); dialogController.hide() } } else null,
            secondaryButtonIcon = d.secondaryIconRes,
            customContent = d.customContent
        )
    }
}