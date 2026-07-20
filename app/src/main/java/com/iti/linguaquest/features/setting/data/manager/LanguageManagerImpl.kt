package com.iti.linguaquest.features.setting.data.manager

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import com.iti.linguaquest.features.setting.domain.manager.LanguageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class LanguageManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : LanguageManager {

    @Suppress("DEPRECATION")
    override fun changeLanguage(languageCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales = LocaleList.forLanguageTags(languageCode)
        } else {
            val locale = Locale.forLanguageTag(languageCode)
            Locale.setDefault(locale)
            val config = context.resources.configuration
            config.setLocale(locale)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }
    }
}
