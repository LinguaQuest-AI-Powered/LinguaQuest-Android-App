package com.iti.linguaquest.features.gallery.domain.usecase

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.gallery.domain.repository.WordRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RefreshGalleryUseCaseTest {

    private lateinit var wordRepository: WordRepository
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var refreshGalleryUseCase: RefreshGalleryUseCase

    @Before
    fun setup() {
        wordRepository = mockk()
        userPreferencesRepository = mockk()
        refreshGalleryUseCase = RefreshGalleryUseCase(wordRepository, userPreferencesRepository)
    }

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceedsAndPreferencesAreNotEmpty() = runTest {
        // Given
        val expectedSource = "Spanish"
        val expectedTarget = "English"
        
        every { userPreferencesRepository.targetLanguageName } returns flowOf(expectedSource)
        every { userPreferencesRepository.nativeLanguageName } returns flowOf(expectedTarget)
        coEvery { 
            wordRepository.refreshGalleryWords(sourceLanguage = expectedSource, targetLanguage = expectedTarget) 
        } returns LinguaQuestResult.Success(Unit)

        // When
        val result = refreshGalleryUseCase()

        // Then
        assertEquals(LinguaQuestResult.Success(Unit), result)
        coVerify(exactly = 1) { 
            wordRepository.refreshGalleryWords(sourceLanguage = expectedSource, targetLanguage = expectedTarget)
        }
    }

    @Test
    fun invoke_returnsSuccessWithDefaultLanguages_whenPreferencesAreEmptyOrBlank() = runTest {
        // Given
        val defaultSource = "English"
        val defaultTarget = "Arabic"
        
        every { userPreferencesRepository.targetLanguageName } returns flowOf("")
        every { userPreferencesRepository.nativeLanguageName } returns flowOf("  ")
        coEvery { 
            wordRepository.refreshGalleryWords(sourceLanguage = defaultSource, targetLanguage = defaultTarget) 
        } returns LinguaQuestResult.Success(Unit)

        // When
        val result = refreshGalleryUseCase()

        // Then
        assertEquals(LinguaQuestResult.Success(Unit), result)
        coVerify(exactly = 1) { 
            wordRepository.refreshGalleryWords(sourceLanguage = defaultSource, targetLanguage = defaultTarget)
        }
    }

    @Test
    fun invoke_returnsFailure_whenRepositoryFails() = runTest {
        // Given
        val defaultSource = "English"
        val defaultTarget = "Arabic"
        val expectedError = LinguaQuestDataError.Remote.NO_INTERNET
        
        every { userPreferencesRepository.targetLanguageName } returns flowOf("")
        every { userPreferencesRepository.nativeLanguageName } returns flowOf("")
        
        coEvery { 
            wordRepository.refreshGalleryWords(sourceLanguage = defaultSource, targetLanguage = defaultTarget) 
        } returns LinguaQuestResult.Failure(expectedError)

        // When
        val result = refreshGalleryUseCase()

        // Then
        assertEquals(LinguaQuestResult.Failure(expectedError), result)
        coVerify(exactly = 1) { 
            wordRepository.refreshGalleryWords(sourceLanguage = defaultSource, targetLanguage = defaultTarget)
        }
    }
}
