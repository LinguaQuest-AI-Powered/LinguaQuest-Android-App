package com.iti.linguaquest.features.notification.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.iti.linguaquest.R
import com.iti.linguaquest.features.notification.domain.model.Notification
import com.iti.linguaquest.features.notification.domain.usecase.DeleteAllNotificationsUseCase
import com.iti.linguaquest.features.notification.domain.usecase.DeleteNotificationUseCase
import com.iti.linguaquest.features.notification.domain.usecase.GetNotificationsUseCase
import com.iti.linguaquest.features.notification.domain.usecase.MarkNotificationAsReadUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.notification.presentation.contract.NotificationEffect
import com.iti.linguaquest.features.notification.presentation.contract.NotificationIntent
import com.iti.linguaquest.features.notification.presentation.contract.NotificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val deleteAllNotificationsUseCase: DeleteAllNotificationsUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
    private val markNotificationAsReadUseCase: MarkNotificationAsReadUseCase,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationState())
    val state: StateFlow<NotificationState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<NotificationEffect>()
    val effect: SharedFlow<NotificationEffect> = _effect.asSharedFlow()

    val notifications: Flow<PagingData<Notification>> = getNotificationsUseCase()
        .cachedIn(viewModelScope)
        .combine(_state.map { it.deletedNotificationIds }.distinctUntilChanged()) { pagingData, deletedIds ->
            pagingData.filter { it.id !in deletedIds }
        }
        .combine(_state.map { it.readNotificationIds }.distinctUntilChanged()) { pagingData, readIds ->
            pagingData.map { item ->
                if (item.id in readIds) item.copy(isRead = true) else item
            }
        }

    fun onIntent(intent: NotificationIntent) {
        when (intent) {
            is NotificationIntent.NotificationClicked -> handleNotificationClicked(intent.notification)
            is NotificationIntent.DeleteNotificationClicked -> _state.update { it.copy(notificationToDelete = intent.id) }
            is NotificationIntent.ConfirmDeleteNotification -> deleteSingleNotification(intent.id)
            is NotificationIntent.DismissDeleteNotificationDialog -> _state.update { it.copy(notificationToDelete = null) }
            is NotificationIntent.DeleteAllClicked -> _state.update { it.copy(showDeleteAllDialog = true) }
            is NotificationIntent.ConfirmDeleteAll -> deleteAllNotifications()
            is NotificationIntent.DismissDeleteAllDialog -> _state.update { it.copy(showDeleteAllDialog = false) }
        }
    }

    private fun handleNotificationClicked(notification: Notification) {
        if (!notification.isRead && notification.id !in _state.value.readNotificationIds) {
            _state.update { it.copy(readNotificationIds = it.readNotificationIds + notification.id) }
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
        _state.update { 
            it.copy(
                deletedNotificationIds = it.deletedNotificationIds + id,
                notificationToDelete = null
            ) 
        }
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
                    _state.update { it.copy(deletedNotificationIds = it.deletedNotificationIds - id) }
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
                    _effect.emit(NotificationEffect.RefreshNotifications)
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
