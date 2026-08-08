package com.iti.linguaquest.features.profile.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.domain.model.UserProfile
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateProfileUseCaseTest {

    private lateinit var profileRepository: ProfileRepository
    private lateinit var useCase: UpdateProfileUseCase

    @Before
    fun setUp() {
        // Given
        profileRepository = mockk(relaxed = true)
        useCase = UpdateProfileUseCase(profileRepository)
    }

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceeds() = runTest {
        // Given
        val username = "testUser"
        val userProfile = mockk<UserProfile>(relaxed = true)
        coEvery { profileRepository.updateProfile(username) } returns LinguaQuestResult.Success(userProfile)

        // When
        val result = useCase(username)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        coVerify { profileRepository.updateProfile(username) }
    }

    @Test
    fun invoke_returnsFailure_whenRepositoryFails() = runTest {
        // Given
        val username = "testUser"
        val expectedError = LinguaQuestResult.Failure(LinguaQuestDataError.Remote.NO_INTERNET)
        coEvery { profileRepository.updateProfile(username) } returns expectedError

        // When
        val result = useCase(username)

        // Then
        assertEquals(expectedError, result)
        coVerify { profileRepository.updateProfile(username) }
    }
}
