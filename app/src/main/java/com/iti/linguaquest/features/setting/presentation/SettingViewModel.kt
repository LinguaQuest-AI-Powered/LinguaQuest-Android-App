package com.iti.linguaquest.features.setting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.connectivity.NetworkMonitor
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.setting.domain.usecase.CancelReminderUseCase
import com.iti.linguaquest.features.setting.domain.usecase.ChangeAppLanguageUseCase
import com.iti.linguaquest.features.setting.domain.usecase.ChangeAppThemeUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetAppLanguageUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetAppThemeUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetNotificationsEnabledUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetReminderDaysUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetReminderEnabledUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetReminderTimeUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetSoundEnabledUseCase
import com.iti.linguaquest.features.setting.domain.usecase.SaveReminderDaysUseCase
import com.iti.linguaquest.features.setting.domain.usecase.SaveReminderEnabledUseCase
import com.iti.linguaquest.features.setting.domain.usecase.SaveReminderTimeUseCase
import com.iti.linguaquest.features.setting.domain.usecase.ScheduleReminderUseCase
import com.iti.linguaquest.features.setting.domain.usecase.ToggleNotificationsUseCase
import com.iti.linguaquest.features.setting.domain.usecase.ToggleSoundUseCase
import com.iti.linguaquest.features.setting.presentation.contract.ReminderEffect
import com.iti.linguaquest.features.setting.presentation.contract.ReminderIntent
import com.iti.linguaquest.features.setting.presentation.contract.ReminderState
import com.iti.linguaquest.features.setting.presentation.contract.RepeatPreset
import com.iti.linguaquest.features.auth.domain.usecase.LogoutUserUseCase
import com.iti.linguaquest.features.auth.domain.usecase.GetAuthLanguagesUseCase
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.setting.presentation.utils.parseDays
import com.iti.linguaquest.features.setting.presentation.utils.toReminderSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject
import com.iti.linguaquest.features.setting.presentation.utils.parseTime


@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val getAppThemeUseCase: GetAppThemeUseCase,
    private val getSoundEnabledUseCase: GetSoundEnabledUseCase,
    private val getNotificationsEnabledUseCase: GetNotificationsEnabledUseCase,
    private val changeAppLanguageUseCase: ChangeAppLanguageUseCase,
    private val changeAppThemeUseCase: ChangeAppThemeUseCase,
    private val toggleSoundUseCase: ToggleSoundUseCase,
    private val toggleNotificationsUseCase: ToggleNotificationsUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val getAuthLanguagesUseCase: GetAuthLanguagesUseCase,
    private val getReminderEnabledUseCase: GetReminderEnabledUseCase,
    private val getReminderTimeUseCase: GetReminderTimeUseCase,
    private val getReminderDaysUseCase: GetReminderDaysUseCase,
    private val saveReminderEnabledUseCase: SaveReminderEnabledUseCase,
    private val saveReminderTimeUseCase: SaveReminderTimeUseCase,
    private val saveReminderDaysUseCase: SaveReminderDaysUseCase,
    private val scheduleReminderUseCase: ScheduleReminderUseCase,
    private val cancelReminderUseCase: CancelReminderUseCase,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    private val snackbarController: SnackbarController
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()

        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )
    val appLanguage: StateFlow<String> = getAppLanguageUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "en")

    val appTheme: StateFlow<String> = getAppThemeUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "system")

    val soundEnabled: StateFlow<Boolean> = getSoundEnabledUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val notificationsEnabled: StateFlow<Boolean> = getNotificationsEnabledUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private val _reminderState = MutableStateFlow(ReminderState())
    val reminderState: StateFlow<ReminderState> = _reminderState.asStateFlow()

    private val _reminderEffect = MutableSharedFlow<ReminderEffect>()
    val reminderEffect: SharedFlow<ReminderEffect> = _reminderEffect.asSharedFlow()

    private val _availableLanguages = MutableStateFlow(LanguagesUiState())
    val availableLanguages: StateFlow<LanguagesUiState> = _availableLanguages.asStateFlow()

    init {
        loadReminderSettings()
        loadLanguages()
    }

    private fun loadLanguages() {
        viewModelScope.launch {
            _availableLanguages.update { it.copy(isLoading = true, isError = false) }
            val result = getAuthLanguagesUseCase()
            if (result is LinguaQuestResult.Success) {
                _availableLanguages.update { it.copy(isLoading = false, languages = result.data, isError = false) }
            } else {
                _availableLanguages.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }
    
    fun retryLoadLanguages() {
        loadLanguages()
    }

    private fun loadReminderSettings() {
        viewModelScope.launch {
            combine(
                getReminderEnabledUseCase(),
                getReminderTimeUseCase(),
                getReminderDaysUseCase()
            ) { enabled, timeStr, daysStr ->
                val (hour, minute) = parseTime(timeStr)
                val selectedDays = parseDays(daysStr)
                ReminderState(
                    enabled = enabled,
                    hour = hour,
                    minute = minute,
                    selectedDays = selectedDays
                )
            }.collect { loadedState ->
                _reminderState.update { current ->
                    current.copy(
                        enabled = loadedState.enabled,
                        hour = loadedState.hour,
                        minute = loadedState.minute,
                        selectedDays = loadedState.selectedDays
                    )
                }
                syncReminderSchedule()
            }
        }
    }

    private val _isLoggingOut = MutableStateFlow(false)
    val isLoggingOut = _isLoggingOut.asStateFlow()

    fun onReminderIntent(intent: ReminderIntent) {
        if (!notificationsEnabled.value) {
            when (intent) {
                ReminderIntent.DismissTimePicker,
                ReminderIntent.DismissRepeatSheet -> Unit
                else -> return
            }
        }

        when (intent) {
            is ReminderIntent.ToggleReminder -> toggleReminder(intent.enabled)
            ReminderIntent.ShowTimePicker -> _reminderState.update { it.copy(showTimePicker = true) }
            ReminderIntent.DismissTimePicker -> _reminderState.update { it.copy(showTimePicker = false) }
            is ReminderIntent.SelectTime -> selectTime(intent.hour, intent.minute)
            ReminderIntent.ShowRepeatSheet -> _reminderState.update { it.copy(showRepeatSheet = true) }
            ReminderIntent.DismissRepeatSheet -> _reminderState.update { it.copy(showRepeatSheet = false) }
            is ReminderIntent.SelectPreset -> applyPreset(intent.preset)
            is ReminderIntent.ToggleDay -> toggleDay(intent.day)
            ReminderIntent.SaveRepeat -> saveRepeat()
        }
    }

    private fun toggleReminder(enabled: Boolean) {
        viewModelScope.launch {
            saveReminderEnabledUseCase(enabled)
            _reminderState.update { it.copy(enabled = enabled) }
            syncReminderSchedule()
            _reminderEffect.emit(
                if (enabled) ReminderEffect.ReminderEnabled else ReminderEffect.ReminderDisabled
            )
            snackbarController.sendEvent(
                SnackbarEvent(
                    title = UiText.StringResource(R.string.daily_reminder),
                    message = UiText.StringResource(
                        if (enabled) R.string.reminder_turned_on else R.string.reminder_turned_off
                    ),
                    type = if (enabled) SnackbarType.SUCCESS else SnackbarType.INFO
                )
            )
        }
    }

    private fun selectTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            val timeStr = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
            saveReminderTimeUseCase(timeStr)
            _reminderState.update { it.copy(hour = hour, minute = minute, showTimePicker = false) }
            syncReminderSchedule()
            _reminderEffect.emit(ReminderEffect.ReminderUpdated)
            snackbarController.sendEvent(
                SnackbarEvent(
                    title = UiText.StringResource(R.string.daily_reminder),
                    message = UiText.StringResource(R.string.reminder_time_updated),
                    type = SnackbarType.SUCCESS
                )
            )
        }
    }

    private fun applyPreset(preset: RepeatPreset) {
        val days = when (preset) {
            RepeatPreset.EVERY_DAY -> DayOfWeek.entries.toSet()
            RepeatPreset.WEEKDAYS -> setOf(
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
            )
            RepeatPreset.WEEKENDS -> setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
            RepeatPreset.CUSTOM -> _reminderState.value.selectedDays
        }
        _reminderState.update { it.copy(selectedDays = days) }
    }

    private fun toggleDay(day: DayOfWeek) {
        _reminderState.update { state ->
            val updated = if (day in state.selectedDays) {
                state.selectedDays - day
            } else {
                state.selectedDays + day
            }
            state.copy(selectedDays = updated)
        }
    }

    private fun saveRepeat() {
        viewModelScope.launch {
            val daysStr = _reminderState.value.selectedDays
                .joinToString(",") { it.value.toString() }
            saveReminderDaysUseCase(daysStr)
            _reminderState.update { it.copy(showRepeatSheet = false) }
            syncReminderSchedule()
            _reminderEffect.emit(ReminderEffect.ReminderUpdated)
            snackbarController.sendEvent(
                SnackbarEvent(
                    title = UiText.StringResource(R.string.daily_reminder),
                    message = UiText.StringResource(R.string.reminder_repeat_updated),
                    type = SnackbarType.SUCCESS
                )
            )
        }
    }

    fun toggleSound(enabled: Boolean) {
        viewModelScope.launch { toggleSoundUseCase(enabled) }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            toggleNotificationsUseCase(enabled)

            if (!enabled) {
                _reminderState.update {
                    it.copy(showTimePicker = false, showRepeatSheet = false)
                }
                cancelReminderUseCase()
                snackbarController.sendEvent(
                    SnackbarEvent(
                        title = UiText.StringResource(R.string.notifications_title),
                        message = UiText.StringResource(R.string.notifications_off_msg),
                        type = SnackbarType.INFO
                    )
                )
                return@launch
            }

            syncReminderSchedule(notificationsAllowed = enabled)
            snackbarController.sendEvent(
                SnackbarEvent(
                    title = UiText.StringResource(R.string.notifications_title),
                    message = UiText.StringResource(R.string.notifications_on_msg),
                    type = SnackbarType.SUCCESS
                )
            )
        }
    }

    fun changeAppLanguage(language: LanguageOption) {
        viewModelScope.launch { changeAppLanguageUseCase(language.id, language.code, language.name) }
    }

    fun changeAppTheme(theme: String) {
        viewModelScope.launch { changeAppThemeUseCase(theme) }
    }
    fun syncReminderSchedule(notificationsAllowed: Boolean = notificationsEnabled.value) {
        val state = _reminderState.value
        if (notificationsAllowed && state.enabled) {
            scheduleReminderUseCase(state.toReminderSettings())
        } else {
            cancelReminderUseCase()
        }
    }



    fun logout(onSuccess: () -> Unit) {
        if (_isLoggingOut.value) return
        _isLoggingOut.value = true
        viewModelScope.launch {
            logoutUserUseCase()
            onSuccess()
            _isLoggingOut.value = false
        }
    }

}