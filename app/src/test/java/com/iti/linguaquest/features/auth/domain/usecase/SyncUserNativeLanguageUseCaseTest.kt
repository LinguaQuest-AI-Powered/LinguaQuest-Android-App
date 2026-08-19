package com.iti.linguaquest.features.auth.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.language.domain.manager.LanguageManager
import com.iti.linguaquest.core.language.domain.usecase.GetSupportedLanguagesUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SyncUserNativeLanguageUseCaseTest {

    private val getSupportedLanguagesUseCase: GetSupportedLanguagesUseCase = mockk()
    private val userPreferencesRepository: UserPreferencesRepository = mockk(relaxed = true)
    private val languageManager: LanguageManager = mockk(relaxed = true)
    private lateinit var useCase: SyncUserNativeLanguageUseCase

    @Before
    fun setUp() {
        useCase = SyncUserNativeLanguageUseCase(
            getSupportedLanguagesUseCase = getSupportedLanguagesUseCase,
            userPreferencesRepository = userPreferencesRepository,
            languageManager = languageManager
        )
    }

    @Test
    fun invoke_savesAndChangesLanguageDirectly_whenLanguageOptionProvided() = runTest {
        // Given
        val languageOption = LanguageOption(id = 1, name = "Arabic", code = "ar", imageUrl = "", isAdded = false)

        // When
        useCase(nativeLanguage = languageOption)

        // Then
        coVerify(exactly = 1) { userPreferencesRepository.saveNativeLanguage(1, "Arabic", "ar") }
        coVerify(exactly = 1) { userPreferencesRepository.saveAppLanguage("ar") }
        coVerify(exactly = 1) { languageManager.changeLanguage("ar") }
    }

    @Test
    fun invoke_fetchesSupportedLanguagesAndMatches_whenNativeLanguageIdProvided() = runTest {
        // Given
        val supportedList = listOf(
            LanguageOption(id = 1, name = "Arabic", code = "ar", imageUrl = "", isAdded = false),
            LanguageOption(id = 2, name = "English", code = "en", imageUrl = "", isAdded = false)
        )
        coEvery { getSupportedLanguagesUseCase() } returns LinguaQuestResult.Success(supportedList)

        // When
        useCase(nativeLanguageId = 2)

        // Then
        coVerify(exactly = 1) { getSupportedLanguagesUseCase() }
        coVerify(exactly = 1) { userPreferencesRepository.saveNativeLanguage(2, "English", "en") }
        coVerify(exactly = 1) { userPreferencesRepository.saveAppLanguage("en") }
        coVerify(exactly = 1) { languageManager.changeLanguage("en") }
    }

    @Test
    fun invoke_doesNothing_whenNoParamsProvided() = runTest {
        // When
        useCase()

        // Then
        coVerify(exactly = 0) { getSupportedLanguagesUseCase() }
        coVerify(exactly = 0) { userPreferencesRepository.saveNativeLanguage(any(), any(), any()) }
        coVerify(exactly = 0) { languageManager.changeLanguage(any()) }
    }
}
