package com.iti.linguaquest.core.sharedComponents.snackbar



import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.ui.graphics.vector.ImageVector
import com.iti.linguaquest.core.sharedComponents.text.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class SnackbarType { INFO, SUCCESS, ERROR, WARNING }

data class SnackbarEvent(
    val message: UiText,
    val title: UiText? = null,
    val type: SnackbarType = SnackbarType.INFO,
    val actionLabel: UiText? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val showCloseIcon: Boolean = false,
    val icon: ImageVector? = null,
    val onAction: (() -> Unit)? = null
)

data class AppSnackbarVisuals(
    override val message: String,
    val title: String? = null,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    val type: SnackbarType = SnackbarType.INFO,
    val showCloseIcon: Boolean = false,
    val icon: ImageVector? = null
) : SnackbarVisuals

interface SnackbarController {
    val events: Flow<SnackbarEvent>
    suspend fun sendEvent(event: SnackbarEvent)
}

@Singleton
class SnackbarControllerImpl @Inject constructor() : SnackbarController {
    private val _events = MutableSharedFlow<SnackbarEvent>()
    override val events = _events.asSharedFlow()
    override suspend fun sendEvent(event: SnackbarEvent) {
        _events.emit(event)
    }
}