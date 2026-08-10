package com.iti.linguaquest.features.notification.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.features.notification.domain.model.Notification
import com.iti.linguaquest.features.notification.domain.usecase.DeleteAllNotificationsUseCase
import com.iti.linguaquest.features.notification.domain.usecase.DeleteNotificationUseCase
import com.iti.linguaquest.features.notification.domain.usecase.GetNotificationsUseCase
import com.iti.linguaquest.features.notification.domain.usecase.MarkNotificationAsReadUseCase
import com.iti.linguaquest.features.notification.domain.usecase.RefreshNotificationsUseCase
import com.iti.linguaquest.features.notification.presentation.contract.NotificationEffect
import com.iti.linguaquest.features.notification.presentation.contract.NotificationIntent
import com.iti.linguaquest.features.notification.presentation.contract.NotificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val refreshNotificationsUseCase: RefreshNotificationsUseCase,
    private val deleteAllNotificationsUseCase: DeleteAllNotificationsUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
    private val markNotificationAsReadUseCase: MarkNotificationAsReadUseCase,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationState())
    val state: StateFlow<NotificationState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<NotificationEffect>()
    val effect: SharedFlow<NotificationEffect> = _effect.asSharedFlow()

    val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

    val notifications: StateFlow<List<Notification>> = getNotificationsUseCase()
        .onEach { list ->
            if (list.isNotEmpty()) {
                _state.update { it.copy(isLoading = false) }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            observeNetworkStatusUseCase()
                .distinctUntilChanged()
                .collect { online ->
                _state.update { it.copy(isOnline = online) }
                if (online) {
                    refreshNotifications()
                } else if (notifications.value.isEmpty()) {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun onIntent(intent: NotificationIntent) {
        when (intent) {
            is NotificationIntent.NotificationClicked -> handleNotificationClicked(intent.notification)
            is NotificationIntent.DeleteNotificationClicked -> handleConfirmDeleteRequest(intent.id)
            is NotificationIntent.ConfirmDeleteNotification -> deleteSingleNotification(intent.id)
            is NotificationIntent.DismissDeleteNotificationDialog -> _state.update { it.copy(notificationToDelete = null) }
            is NotificationIntent.DeleteAllClicked -> handleConfirmDeleteAllRequest()
            is NotificationIntent.ConfirmDeleteAll -> deleteAllNotifications()
            is NotificationIntent.DismissDeleteAllDialog -> _state.update { it.copy(showDeleteAllDialog = false) }
        }
    }

    private fun refreshNotifications() {
        viewModelScope.launch {
            if (!isOnline.value && notifications.value.isEmpty()) {
                _state.update { it.copy(isLoading = false) }
                return@launch
            }
            if (notifications.value.isEmpty()) {
                _state.update { it.copy(isLoading = true) }
            }
            when (val result = refreshNotificationsUseCase()) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                }
                is LinguaQuestResult.Failure -> {
                    _state.update { it.copy(isLoading = false) }
                    if (notifications.value.isEmpty() && isOnline.value) {
                        snackbarController.sendEvent(
                            SnackbarEvent(
                                message = result.error.toUiText(),
                                type = SnackbarType.ERROR
                            )
                        )
                    }
                }
            }
        }
    }

    private fun handleConfirmDeleteRequest(id: Long) {
        if (!isOnline.value) return
        _state.update { it.copy(notificationToDelete = id) }
    }

    private fun handleConfirmDeleteAllRequest() {
        if (!isOnline.value) return
        _state.update { it.copy(showDeleteAllDialog = true) }
    }

    private fun handleNotificationClicked(notification: Notification) {
        if (!isOnline.value) return
        if (!notification.isRead) {
            viewModelScope.launch {
                when (val result = markNotificationAsReadUseCase(notification.id)) {
                    is LinguaQuestResult.Success -> {}
                    is LinguaQuestResult.Failure -> {
                        snackbarController.sendEvent(
                            SnackbarEvent(
                                message = result.error.toUiText(),
                                type = SnackbarType.ERROR
                            )
                        )
                    }
                }
            }
        }
    }

    private fun deleteSingleNotification(id: Long) {
        if (!isOnline.value) return
        _state.update { it.copy(notificationToDelete = null) }
        viewModelScope.launch {
            when (val result = deleteNotificationUseCase(id)) {
                is LinguaQuestResult.Success -> {
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = UiText.StringResource(R.string.notification_deleted_toast),
                            type = SnackbarType.SUCCESS
                        )
                    )
                }
                is LinguaQuestResult.Failure -> {
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = result.error.toUiText(),
                            type = SnackbarType.ERROR
                        )
                    )
                }
            }
        }
    }

    private fun deleteAllNotifications() {
        if (!isOnline.value) return
        _state.update { it.copy(showDeleteAllDialog = false, isDeleting = true) }
        viewModelScope.launch {
            when (val result = deleteAllNotificationsUseCase()) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isDeleting = false) }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = UiText.StringResource(R.string.notifications_all_deleted_toast),
                            type = SnackbarType.SUCCESS
                        )
                    )
                }
                is LinguaQuestResult.Failure -> {
                    _state.update { it.copy(isDeleting = false) }
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = result.error.toUiText(),
                            type = SnackbarType.ERROR
                        )
                    )
                }
            }
        }
    }
}
