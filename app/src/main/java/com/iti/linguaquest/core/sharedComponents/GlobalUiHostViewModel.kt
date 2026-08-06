package com.iti.linguaquest.core.sharedComponents



import androidx.lifecycle.ViewModel
import com.iti.linguaquest.core.session.SessionEventBus
import com.iti.linguaquest.core.sharedComponents.dialog.DialogController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GlobalUiHostViewModel @Inject constructor(
    val snackbarController: SnackbarController,
    val dialogController: DialogController,
    val sessionEventBus: SessionEventBus
) : ViewModel()