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
        AlertDialog(
            onDismissRequest = { d.onDismiss?.invoke(); dialogController.hide() },
            title = { Text(d.title.asString()) },
            text = { Text(d.message.asString()) },
            confirmButton = {
                TextButton(onClick = { d.onConfirm?.invoke(); dialogController.hide() }) {
                    Text(d.confirmText.asString())
                }
            },
            dismissButton = d.dismissText?.let { text ->
                {
                    TextButton(onClick = { d.onDismiss?.invoke(); dialogController.hide() }) {
                        Text(text.asString())
                    }
                }
            }
        )
    }
}