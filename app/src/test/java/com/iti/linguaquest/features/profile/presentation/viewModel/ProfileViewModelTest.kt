package com.iti.linguaquest.features.profile.presentation.viewModel

import android.net.Uri
import app.cash.turbine.test
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.wallet.domain.usecase.RefreshWalletUseCase
import com.iti.linguaquest.features.profile.domain.usecase.GetCachedProfileUseCase
import com.iti.linguaquest.features.profile.domain.usecase.PreloadImageUseCase
import com.iti.linguaquest.features.profile.domain.usecase.RefreshProfileSummaryUseCase
import com.iti.linguaquest.features.profile.domain.usecase.UploadAvatarUseCase
import com.iti.linguaquest.features.profile.presentation.contract.ProfileEffect
import com.iti.linguaquest.features.profile.presentation.contract.ProfileIntent
import com.iti.linguaquest.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getCachedProfileUseCase: GetCachedProfileUseCase
    private lateinit var refreshProfileSummaryUseCase: RefreshProfileSummaryUseCase
    private lateinit var uploadAvatarUseCase: UploadAvatarUseCase
    private lateinit var snackbarController: SnackbarController
    private lateinit var preloadImageUseCase: PreloadImageUseCase
    private lateinit var observeNetworkStatusUseCase: ObserveNetworkStatusUseCase
    private lateinit var refreshWalletUseCase: RefreshWalletUseCase
    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        // Given
        getCachedProfileUseCase = mockk(relaxed = true)
        refreshProfileSummaryUseCase = mockk(relaxed = true)
        uploadAvatarUseCase = mockk(relaxed = true)
        snackbarController = mockk(relaxed = true)
        preloadImageUseCase = mockk(relaxed = true)
        observeNetworkStatusUseCase = mockk(relaxed = true)
        refreshWalletUseCase = mockk(relaxed = true)

        every { observeNetworkStatusUseCase() } returns flowOf(true)
        every { getCachedProfileUseCase() } returns flowOf(null)
        coEvery { refreshProfileSummaryUseCase() } returns LinguaQuestResult.Success(Unit)
        coEvery { refreshWalletUseCase() } returns LinguaQuestResult.Success(Unit)

        viewModel = ProfileViewModel(
            getCachedProfileUseCase,
            refreshProfileSummaryUseCase,
            uploadAvatarUseCase,
            snackbarController,
            preloadImageUseCase,
            observeNetworkStatusUseCase,
            refreshWalletUseCase
        )
    }

    @Test
    fun onIntent_emitsNavigateToSettings_whenSettingsClicked() = runTest {
        // Given
        val intent = ProfileIntent.SettingsClicked

        viewModel.effect.test {
            // When
            viewModel.onIntent(intent)

            // Then
            val effect = awaitItem()
            assertEquals(ProfileEffect.NavigateToSettings, effect)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onIntent_emitsNavigateToAllAchievements_whenViewAllAchievementsClicked() = runTest {
        // Given
        val intent = ProfileIntent.ViewAllAchievementsClicked

        viewModel.effect.test {
            // When
            viewModel.onIntent(intent)

            // Then
            val effect = awaitItem()
            assertEquals(ProfileEffect.NavigateToAllAchievements, effect)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onIntent_emitsNavigateToAllLeaderboard_whenViewAllLeaderboardClicked() = runTest {
        // Given
        val intent = ProfileIntent.ViewAllLeaderboardClicked

        viewModel.effect.test {
            // When
            viewModel.onIntent(intent)

            // Then
            val effect = awaitItem()
            assertEquals(ProfileEffect.NavigateToAllLeaderboard, effect)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onIntent_callsUploadAvatar_whenAvatarPicked() = runTest {
        // Given
        val mockUri = mockk<Uri>()
        coEvery { uploadAvatarUseCase(mockUri) } returns LinguaQuestResult.Success("https://new.avatar/url")
        val intent = ProfileIntent.AvatarPicked(mockUri)

        // When
        viewModel.onIntent(intent)

        // Then
        io.mockk.coVerify { uploadAvatarUseCase(mockUri) }
    }
}
