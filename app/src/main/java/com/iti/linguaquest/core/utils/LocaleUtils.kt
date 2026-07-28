package com.iti.linguaquest.core.utils


import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.Locale
import androidx.core.content.edit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

object LocaleUtils {
    private const val PREFS_NAME = "locale_prefs"
    private const val KEY_APP_LANGUAGE = "app_language_code"

    fun saveLanguage(context: Context, languageCode: String) {
        prefs(context).edit { putString(KEY_APP_LANGUAGE, languageCode) }
    }

    fun getSavedLanguage(context: Context): String =
        prefs(context).getString(KEY_APP_LANGUAGE, Locale.getDefault().language) ?: "en"

    fun wrapContext(base: Context): Context {
        val locale = Locale.forLanguageTag(getSavedLanguage(base))
        Locale.setDefault(locale)
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        return base.createConfigurationContext(config)
    }

    fun languageFlow(context: Context): Flow<String> = callbackFlow {
        val sp = prefs(context)
        trySend(getSavedLanguage(context))
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { p, key ->
            if (key == KEY_APP_LANGUAGE) trySend(p.getString(KEY_APP_LANGUAGE, "en") ?: "en")
        }
        sp.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { sp.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}