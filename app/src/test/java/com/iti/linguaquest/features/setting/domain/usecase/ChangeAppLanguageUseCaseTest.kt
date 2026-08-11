package com.iti.linguaquest.features.setting.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.features.home.domain.repository.LanguagesRepo
import com.iti.linguaquest.core.language.domain.manager.LanguageManager
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ChangeAppLanguageUseCaseTest {

    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var languageManager: LanguageManager
    private lateinit var languagesRepo: LanguagesRepo
    private lateinit var useCase: ChangeAppLanguageUseCase

    @Before
    fun setUp() {
        // Given
        userPreferencesRepository = mockk(relaxed = true)
        languageManager = mockk(relaxed = true)
        languagesRepo = mockk(relaxed = true)
        useCase = ChangeAppLanguageUseCase(
            userPreferencesRepository = userPreferencesRepository,
            languageManager = languageManager,
            languagesRepo = languagesRepo
        )
    }

    @Test
    fun invoke_updatesLanguageSettingsAndNativeLanguage_whenCalled() = runTest {
        // Given
        val languageId = 2
        val languageCode = "es"
        val languageName = "Spanish"

        // When
        useCase(languageId, languageCode, languageName)

        // Then
        coVerify { userPreferencesRepository.saveAppLanguage(languageCode) }
        coVerify { userPreferencesRepository.saveNativeLanguage(languageId, languageName) }
        coVerify { languageManager.changeLanguage(languageCode) }
        coVerify { languagesRepo.setNativeLanguage(languageId) }
    }
}
