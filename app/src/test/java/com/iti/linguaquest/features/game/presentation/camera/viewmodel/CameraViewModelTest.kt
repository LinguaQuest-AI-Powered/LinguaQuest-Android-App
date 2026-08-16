package com.iti.linguaquest.features.game.presentation.camera.viewmodel

import android.net.Uri
import app.cash.turbine.test
import com.iti.linguaquest.features.game.presentation.camera.contract.CameraEffect
import com.iti.linguaquest.features.game.presentation.camera.contract.CameraIntent
import com.iti.linguaquest.features.game.presentation.camera.contract.PermissionStatus
import com.iti.linguaquest.util.MainDispatcherRule
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class CameraViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: CameraViewModel

    @Before
    fun setUp() {
        viewModel = CameraViewModel()
    }

    @Test
    fun onIntent_updatesPermissionStatus_whenPermissionResultIntent() = runTest {
        // Given
        val status = PermissionStatus.GRANTED

        // When
        viewModel.onIntent(CameraIntent.PermissionResult(status))

        // Then
        viewModel.state.test {
            assertEquals(status, awaitItem().permissionStatus)
        }
    }

    @Test
    fun onIntent_sendsRequestPermissionEffect_whenGrantPermissionClickedAndNotPermanentlyDenied() = runTest {
        // When
        viewModel.onIntent(CameraIntent.GrantPermissionClicked)

        // Then
        viewModel.effect.test {
            assertEquals(CameraEffect.RequestCameraPermission, awaitItem())
        }
    }

    @Test
    fun onIntent_sendsOpenAppSettingsEffect_whenGrantPermissionClickedAndPermanentlyDenied() = runTest {
        // Given
        viewModel.onIntent(CameraIntent.PermissionResult(PermissionStatus.PERMANENTLY_DENIED))

        // When
        viewModel.onIntent(CameraIntent.GrantPermissionClicked)

        // Then
        viewModel.effect.test {
            assertEquals(CameraEffect.OpenAppSettings, awaitItem())
        }
    }

    @Test
    fun onIntent_updatesCapturedUri_whenCapturePhotoIntent() = runTest {
        // Given
        val uri = mockk<Uri>(relaxed = true)

        // When
        viewModel.onIntent(CameraIntent.CapturePhoto(uri))

        // Then
        viewModel.state.test {
            assertEquals(uri, awaitItem().capturedUri)
        }
    }

    @Test
    fun onIntent_clearsCapturedUri_whenRetryCaptureIntent() = runTest {
        // Given
        val uri = mockk<Uri>(relaxed = true)
        viewModel.onIntent(CameraIntent.CapturePhoto(uri))

        // When
        viewModel.onIntent(CameraIntent.RetryCapture)

        // Then
        viewModel.state.test {
            assertEquals(null, awaitItem().capturedUri)
        }
    }

    @Test
    fun onIntent_sendsNavigateToProcessingEffect_whenSubmitPhotoIntent() = runTest {
        // Given
        val uri = mockk<Uri>(relaxed = true)
        viewModel.onIntent(CameraIntent.CapturePhoto(uri))

        // When
        viewModel.onIntent(CameraIntent.SubmitPhoto())

        // Then
        viewModel.effect.test {
            assertEquals(CameraEffect.NavigateToProcessing(uri), awaitItem())
        }
    }

    @Test
    fun onIntent_togglesCameraLens_whenToggleCameraLensIntent() = runTest {
        // Given initial state is front camera true
        val initialState = viewModel.state.value.isFrontCamera

        // When
        viewModel.onIntent(CameraIntent.ToggleCameraLens)

        // Then
        viewModel.state.test {
            assertEquals(!initialState, awaitItem().isFrontCamera)
        }
    }

    @Test
    fun onIntent_togglesFlash_whenToggleFlashIntent() = runTest {
        // Given initial state is flash disabled
        val initialState = viewModel.state.value.isFlashEnabled

        // When
        viewModel.onIntent(CameraIntent.ToggleFlash)

        // Then
        viewModel.state.test {
            assertEquals(!initialState, awaitItem().isFlashEnabled)
        }
    }

    @Test
    fun onIntent_sendsNavigateBackEffect_whenBackClickedIntent() = runTest {
        // When
        viewModel.onIntent(CameraIntent.BackClicked)

        // Then
        viewModel.effect.test {
            assertEquals(CameraEffect.NavigateBack, awaitItem())
        }
    }
}
