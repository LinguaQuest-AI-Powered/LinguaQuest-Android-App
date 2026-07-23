package com.iti.linguaquest.features.setting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.setting.domain.usecase.ChangeAppLanguageUseCase
import com.iti.linguaquest.features.setting.domain.usecase.ChangeAppThemeUseCase
import com.iti.linguaquest.features.setting.domain.usecase.ToggleSoundUseCase
import com.iti.linguaquest.features.setting.domain.usecase.ToggleNotificationsUseCase
import com.iti.linguaquest.features.auth.domain.usecase.LogoutUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.iti.linguaquest.features.setting.domain.usecase.GetAppLanguageUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetAppThemeUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetSoundEnabledUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetNotificationsEnabledUseCase

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
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {

    val appLanguage: StateFlow<String> = getAppLanguageUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "en"
        )

    val appTheme: StateFlow<String> = getAppThemeUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "system"
        )

    val soundEnabled: StateFlow<Boolean> = getSoundEnabledUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val notificationsEnabled: StateFlow<Boolean> = getNotificationsEnabledUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    private val _isLoggingOut = MutableStateFlow(false)
    val isLoggingOut = _isLoggingOut.asStateFlow()

    fun toggleSound(enabled: Boolean) {
        viewModelScope.launch {
            toggleSoundUseCase(enabled)
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            toggleNotificationsUseCase(enabled)
        }
    }

    fun changeAppLanguage(language: String) {
        viewModelScope.launch {
            changeAppLanguageUseCase(language)
        }
    }

    fun changeAppTheme(theme: String) {
        viewModelScope.launch {
            changeAppThemeUseCase(theme)
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
