package com.iti.linguaquest.features.profile.data.repository

import android.net.Uri
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.data.datasource.local.ProfileLocalDataSource
import com.iti.linguaquest.features.profile.data.datasource.remote.EditProfileRemoteDataSource
import com.iti.linguaquest.features.profile.data.datasource.remote.ProfileRemoteDataSource
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.UpdateProfileRequestDto
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary
import com.iti.linguaquest.features.profile.domain.model.UserProfile
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProfileRepositoryImplTest {

    private lateinit var remoteDataSource: ProfileRemoteDataSource
    private lateinit var localDataSource: ProfileLocalDataSource
    private lateinit var eRemoteDataSource: EditProfileRemoteDataSource
    private lateinit var repository: ProfileRepositoryImpl

    @Before
    fun setUp() {
        // Given
        remoteDataSource = mockk(relaxed = true)
        localDataSource = mockk(relaxed = true)
        eRemoteDataSource = mockk(relaxed = true)

        val cachedProfileSummary = mockk<ProfileSummary>(relaxed = true)
        every { localDataSource.cachedProfile } returns flowOf(cachedProfileSummary)

        repository = ProfileRepositoryImpl(
            remoteDataSource,
            localDataSource,
            eRemoteDataSource
        )
    }

    @Test
    fun refreshProfileSummary_savesProfile_whenRemoteSuccess() = runTest {
        // Given
        val profileSummary = mockk<ProfileSummary>(relaxed = true)
        coEvery { remoteDataSource.getProfileSummary() } returns LinguaQuestResult.Success(profileSummary)

        // When
        val result = repository.refreshProfileSummary()

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        coVerify { localDataSource.saveProfile(profileSummary) }
    }

    @Test
    fun refreshProfileSummary_returnsFailure_whenRemoteFails() = runTest {
        // Given
        val failure = LinguaQuestResult.Failure(LinguaQuestDataError.Remote.NO_INTERNET)
        coEvery { remoteDataSource.getProfileSummary() } returns failure

        // When
        val result = repository.refreshProfileSummary()

        // Then
        assertEquals(failure, result)
        coVerify(exactly = 0) { localDataSource.saveProfile(any()) }
    }

    @Test
    fun uploadAvatar_updatesCachedProfile_whenUploadSuccess() = runTest {
        // Given
        val uri = mockk<Uri>()
        val newPhotoUrl = "https://newphoto.url"
        coEvery { remoteDataSource.uploadAvatar(uri) } returns LinguaQuestResult.Success(newPhotoUrl)

        // When
        val result = repository.uploadAvatar(uri)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        coVerify { localDataSource.saveProfile(any()) }
    }

    @Test
    fun updateProfile_refreshesProfileSummary_whenUpdateSuccess() = runTest {
        // Given
        val username = "newUsername"
        val request = UpdateProfileRequestDto(username = username)
        val profileDto = mockk<com.iti.linguaquest.features.profile.data.datasource.remote.dto.ProfileDto>(relaxed = true)
        coEvery { eRemoteDataSource.updateProfile(request) } returns LinguaQuestResult.Success(profileDto)
        coEvery { remoteDataSource.getProfileSummary() } returns LinguaQuestResult.Success(mockk(relaxed = true))

        // When
        val result = repository.updateProfile(username)

        // Then
        assertTrue(result is LinguaQuestResult.Success)
        coVerify { remoteDataSource.getProfileSummary() }
    }
}
