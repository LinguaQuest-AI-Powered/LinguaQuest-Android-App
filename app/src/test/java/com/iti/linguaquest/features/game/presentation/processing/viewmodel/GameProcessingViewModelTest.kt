package com.iti.linguaquest.features.game.presentation.processing.viewmodel

import app.cash.turbine.test
import com.iti.linguaquest.R
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.features.game.domain.model.VerifyLevelResult
import com.iti.linguaquest.features.game.domain.usecase.SaveVaultImageUseCase
import com.iti.linguaquest.features.game.domain.usecase.VerifyLevelUseCase
import com.iti.linguaquest.features.game.presentation.processing.contract.GameProcessingEffect
import com.iti.linguaquest.features.game.presentation.processing.contract.GameProcessingIntent
import com.iti.linguaquest.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class GameProcessingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val verifyLevelUseCase: VerifyLevelUseCase = mockk()
    private val saveVaultImageUseCase: SaveVaultImageUseCase = mockk(relaxed = true)

    private lateinit var viewModel: GameProcessingViewModel

    @Before
    fun setUp() {
        viewModel = GameProcessingViewModel(verifyLevelUseCase, saveVaultImageUseCase)
    }

    @Test
    fun verifyImage_sendsNavigateToErrorEffect_whenImageFileIsNull() = runTest {
        // When
        viewModel.verifyImage(1, 1, "Apple", "Spanish", null)

        // Then
        viewModel.effect.test {
            val effect = awaitItem()
            assertTrue(effect is GameProcessingEffect.NavigateToError)
            assertEquals(
                UiText.StringResource(R.string.game_processing_simulate_error_message),
                (effect as GameProcessingEffect.NavigateToError).errorMessage
            )
        }
    }

    @Test
    fun verifyImage_sendsNavigateToErrorEffect_whenImageFileDoesNotExist() = runTest {
        // Given
        val file = mockk<File>()
        every { file.exists() } returns false

        // When
        viewModel.verifyImage(1, 1, "Apple", "Spanish", file)

        // Then
        viewModel.effect.test {
            val effect = awaitItem()
            assertTrue(effect is GameProcessingEffect.NavigateToError)
        }
    }

    @Test
    fun verifyImage_sendsNavigateToSuccessEffectAndSavesImage_whenMatchIsTrue() = runTest {
        // Given
        val file = mockk<File>()
        every { file.exists() } returns true
        val worldId = 1
        val levelId = 1
        val targetWord = "Apple"
        val targetLanguage = "Spanish"
        val expectedResult = VerifyLevelResult(
            isMatch = true,
            xpEarned = 100,
            coinsEarned = 50,
            level = 2,
            levelProgressPercentage = 50
        )
        coEvery { verifyLevelUseCase(worldId, levelId, file) } returns LinguaQuestResult.Success(expectedResult)

        // When
        viewModel.verifyImage(worldId, levelId, targetWord, targetLanguage, file)

        // Then
        viewModel.effect.test {
            val effect = awaitItem()
            assertTrue(effect is GameProcessingEffect.NavigateToSuccess)
            effect as GameProcessingEffect.NavigateToSuccess
            assertEquals(100, effect.xp)
            assertEquals(50, effect.coins)
            assertEquals(2, effect.level)
            assertEquals(50, effect.progressPercentage)
        }
        coVerify(exactly = 1) { saveVaultImageUseCase(targetWord, targetLanguage, file) }
    }

    @Test
    fun verifyImage_sendsNavigateToFailureEffect_whenMatchIsFalse() = runTest {
        // Given
        val file = mockk<File>()
        every { file.exists() } returns true
        val worldId = 1
        val levelId = 1
        val targetWord = "Apple"
        val targetLanguage = "Spanish"
        val expectedResult = VerifyLevelResult(
            isMatch = false,
            xpEarned = 0,
            coinsEarned = 0,
            level = 0,
            levelProgressPercentage = 0
        )
        coEvery { verifyLevelUseCase(worldId, levelId, file) } returns LinguaQuestResult.Success(expectedResult)

        // When
        viewModel.verifyImage(worldId, levelId, targetWord, targetLanguage, file)

        // Then
        viewModel.effect.test {
            val effect = awaitItem()
            assertTrue(effect is GameProcessingEffect.NavigateToFailure)
        }
        coVerify(exactly = 0) { saveVaultImageUseCase(any(), any(), any()) }
    }

    @Test
    fun verifyImage_sendsNavigateToErrorEffect_whenVerifyLevelUseCaseFails() = runTest {
        // Given
        val file = mockk<File>()
        every { file.exists() } returns true
        val worldId = 1
        val levelId = 1
        val targetWord = "Apple"
        val targetLanguage = "Spanish"
        val error = LinguaQuestDataError.Remote.SERVER
        coEvery { verifyLevelUseCase(worldId, levelId, file) } returns LinguaQuestResult.Failure(error)

        // When
        viewModel.verifyImage(worldId, levelId, targetWord, targetLanguage, file)

        // Then
        viewModel.effect.test {
            val effect = awaitItem()
            assertTrue(effect is GameProcessingEffect.NavigateToError)
        }
    }
}
