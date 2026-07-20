package com.iti.linguaquest.features.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

import android.content.Context
import android.os.Build
import android.app.LocaleManager
import android.os.LocaleList
import java.util.Locale

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val appLanguage: StateFlow<String> = userPreferencesRepository.appLanguage
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "en"
        )

    fun changeAppLanguage(context: Context, language: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveAppLanguage(language)
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.getSystemService(LocaleManager::class.java).applicationLocales = LocaleList.forLanguageTags(language)
            } else {
                val locale = Locale(language)
                Locale.setDefault(locale)
                val config = context.resources.configuration
                config.setLocale(locale)
                context.resources.updateConfiguration(config, context.resources.displayMetrics)
                
                // Restart activity if needed (omitted for simplicity, updateConfiguration often handles it in pure Compose if LocalConfiguration is triggered)
            }
        }
    }
}
