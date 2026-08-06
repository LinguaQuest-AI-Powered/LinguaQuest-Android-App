package com.iti.linguaquest.core.sharedComponents



import androidx.lifecycle.ViewModel
import com.iti.linguaquest.core.session.SessionEventBus
import com.iti.linguaquest.core.sharedComponents.dialog.DialogController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class GlobalUiHostViewModel @Inject constructor(
    val snackbarController: SnackbarController,
    val dialogController: DialogController,
    val sessionEventBus: SessionEventBus,
    val notificationBannerController: NotificationBannerController
) : ViewModel() {

}