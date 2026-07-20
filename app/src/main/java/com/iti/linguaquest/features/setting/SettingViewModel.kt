package com.iti.linguaquest.features.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.preferences.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.features.setting.domain.usecase.ChangeAppLanguageUseCase
import com.iti.linguaquest.features.setting.domain.usecase.ToggleSoundUseCase
import com.iti.linguaquest.features.setting.domain.usecase.ToggleNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository,
    private val changeAppLanguageUseCase: ChangeAppLanguageUseCase,
    private val toggleSoundUseCase: ToggleSoundUseCase,
    private val toggleNotificationsUseCase: ToggleNotificationsUseCase
) : ViewModel() {

    val appLanguage: StateFlow<String> = userPreferencesRepository.appLanguage
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "en"
        )

    val soundEnabled: StateFlow<Boolean> = userPreferencesRepository.soundEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val notificationsEnabled: StateFlow<Boolean> = userPreferencesRepository.notificationsEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

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
}
