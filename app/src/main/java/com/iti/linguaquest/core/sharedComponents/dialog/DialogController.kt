package com.iti.linguaquest.core.sharedComponents.dialog



import com.iti.linguaquest.core.sharedComponents.text.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class DialogUiState(
    val title: UiText,
    val message: UiText,
    val confirmText: UiText = UiText.DynamicString("OK"),
    val dismissText: UiText? = null,
    val onConfirm: (() -> Unit)? = null,
    val onDismiss: (() -> Unit)? = null
)

interface DialogController {
    val state: StateFlow<DialogUiState?>
    fun show(dialog: DialogUiState)
    fun hide()
}

@Singleton
class DialogControllerImpl @Inject constructor() : DialogController {
    private val _state = MutableStateFlow<DialogUiState?>(null)
    override val state: StateFlow<DialogUiState?> = _state.asStateFlow()

    override fun show(dialog: DialogUiState) { _state.value = dialog }
    override fun hide() { _state.value = null }
}