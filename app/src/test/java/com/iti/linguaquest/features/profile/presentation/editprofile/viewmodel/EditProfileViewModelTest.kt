package com.iti.linguaquest.features.profile.presentation.editprofile.viewmodel

import android.net.Uri
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.features.profile.domain.model.UserProfile
import com.iti.linguaquest.features.profile.domain.usecase.ChangePasswordUseCase
import com.iti.linguaquest.features.profile.domain.usecase.GetCachedProfileUseCase
import com.iti.linguaquest.features.profile.domain.usecase.PreloadImageUseCase
import com.iti.linguaquest.features.profile.domain.usecase.UpdateProfileUseCase
import com.iti.linguaquest.features.profile.domain.usecase.UploadAvatarUseCase
import com.iti.linguaquest.features.profile.presentation.editprofile.contract.EditProfileIntent
import com.iti.linguaquest.features.profile.presentation.editprofile.contract.EditProfileTab
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

class EditProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var updateProfileUseCase: UpdateProfileUseCase
    private lateinit var changePasswordUseCase: ChangePasswordUseCase
    private lateinit var uploadAvatarUseCase: UploadAvatarUseCase
    private lateinit var snackbarController: SnackbarController
    private lateinit var getCachedProfileUseCase: GetCachedProfileUseCase
    private lateinit var observeNetworkStatusUseCase: ObserveNetworkStatusUseCase
    private lateinit var preloadImageUseCase: PreloadImageUseCase
    private lateinit var viewModel: EditProfileViewModel

    @Before
    fun setUp() {
        // Given
        updateProfileUseCase = mockk(relaxed = true)
        changePasswordUseCase = mockk(relaxed = true)
        uploadAvatarUseCase = mockk(relaxed = true)
        snackbarController = mockk(relaxed = true)
        getCachedProfileUseCase = mockk(relaxed = true)
        observeNetworkStatusUseCase = mockk(relaxed = true)
        preloadImageUseCase = mockk(relaxed = true)

        every { observeNetworkStatusUseCase() } returns flowOf(true)
        every { getCachedProfileUseCase() } returns flowOf(null)

        viewModel = EditProfileViewModel(
            updateProfileUseCase,
            changePasswordUseCase,
            uploadAvatarUseCase,
            snackbarController,
            getCachedProfileUseCase,
            observeNetworkStatusUseCase,
            preloadImageUseCase
        )
    }

    @Test
    fun onIntent_updatesDisplayName_whenOnDisplayNameChanged() {
        // Given
        val newName = "New Name"
        val intent = EditProfileIntent.OnDisplayNameChanged(newName)

        // When
        viewModel.onIntent(intent)

        // Then
        assertEquals(newName, viewModel.state.value.displayName)
    }

    @Test
    fun onIntent_updatesOldPassword_whenOnOldPasswordChanged() {
        // Given
        val oldPassword = "oldPassword"
        val intent = EditProfileIntent.OnOldPasswordChanged(oldPassword)

        // When
        viewModel.onIntent(intent)

        // Then
        assertEquals(oldPassword, viewModel.state.value.oldPassword)
    }

    @Test
    fun onIntent_updatesNewPassword_whenOnNewPasswordChanged() {
        // Given
        val newPassword = "newPassword"
        val intent = EditProfileIntent.OnNewPasswordChanged(newPassword)

        // When
        viewModel.onIntent(intent)

        // Then
        assertEquals(newPassword, viewModel.state.value.newPassword)
    }

    @Test
    fun onIntent_updatesTab_whenOnTabChanged() {
        // Given
        val newTab = EditProfileTab.SECURITY
        val intent = EditProfileIntent.OnTabChanged(newTab)

        // When
        viewModel.onIntent(intent)

        // Then
        assertEquals(newTab, viewModel.state.value.selectedTab)
    }

    @Test
    fun onIntent_callsUpdateProfile_whenSaveCurrentTabChangesAndTabIsPersonalInfo() = runTest {
        // Given
        viewModel.onIntent(EditProfileIntent.OnTabChanged(EditProfileTab.PERSONAL_INFO))
        viewModel.onIntent(EditProfileIntent.OnDisplayNameChanged("TestUser"))
        
        coEvery { updateProfileUseCase("TestUser") } returns LinguaQuestResult.Success(mockk(relaxed = true))
        
        // When
        viewModel.onIntent(EditProfileIntent.SaveCurrentTabChanges)

        // Then
        io.mockk.coVerify { updateProfileUseCase("TestUser") }
    }
}
