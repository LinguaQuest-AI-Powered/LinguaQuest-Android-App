package com.iti.linguaquest.features.setting.data.manager

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import java.util.Locale
import com.iti.linguaquest.core.language.data.manager.LanguageManagerImpl

class LanguageManagerImplTest {

    private lateinit var context: Context
    private lateinit var resources: Resources
    private lateinit var configuration: Configuration
    private lateinit var displayMetrics: DisplayMetrics
    private lateinit var languageManager: LanguageManagerImpl

    @Before
    fun setUp() {
        // Given
        context = mockk(relaxed = true)
        resources = mockk(relaxed = true)
        configuration = mockk(relaxed = true)
        displayMetrics = mockk(relaxed = true)

        every { context.resources } returns resources
        every { resources.configuration } returns configuration
        every { resources.displayMetrics } returns displayMetrics

        languageManager = LanguageManagerImpl(context)
    }

    @Test
    fun changeLanguage_updatesConfiguration_whenSdkIsBelowTiramisu() {
        // Given
        val languageCode = "es"

        // When
        languageManager.changeLanguage(languageCode)

        // Then
        // Without Robolectric, Build.VERSION.SDK_INT is 0, so the else branch executes.
        verify { configuration.setLocale(Locale.forLanguageTag(languageCode)) }
        verify { resources.updateConfiguration(configuration, displayMetrics) }
        assert(Locale.getDefault().toLanguageTag() == languageCode || Locale.getDefault().language == languageCode)
    }
}
