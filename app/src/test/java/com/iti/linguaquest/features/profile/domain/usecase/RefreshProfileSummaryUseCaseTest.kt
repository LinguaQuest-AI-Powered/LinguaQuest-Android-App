package com.iti.linguaquest.features.profile.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RefreshProfileSummaryUseCaseTest {

    private lateinit var profileRepository: ProfileRepository
    private lateinit var useCase: RefreshProfileSummaryUseCase

    @Before
    fun setUp() {
        // Given
        profileRepository = mockk(relaxed = true)
        useCase = RefreshProfileSummaryUseCase(profileRepository)
    }

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceeds() = runTest {
        // Given
        coEvery { profileRepository.refreshProfileSummary() } returns LinguaQuestResult.Success(Unit)

        // When
        val result = useCase()

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        coVerify { profileRepository.refreshProfileSummary() }
    }

    @Test
    fun invoke_returnsFailure_whenRepositoryFails() = runTest {
        // Given
        val expectedError = LinguaQuestResult.Failure(LinguaQuestDataError.Remote.NO_INTERNET)
        coEvery { profileRepository.refreshProfileSummary() } returns expectedError

        // When
        val result = useCase()

        // Then
        assertEquals(expectedError, result)
        coVerify { profileRepository.refreshProfileSummary() }
    }
}
