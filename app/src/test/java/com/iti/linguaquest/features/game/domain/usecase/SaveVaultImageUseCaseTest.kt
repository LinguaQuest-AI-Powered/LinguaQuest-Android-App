package com.iti.linguaquest.features.game.domain.usecase

import com.iti.linguaquest.core.utils.VaultImageStorageManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File

class SaveVaultImageUseCaseTest {

    private val vaultImageStorageManager: VaultImageStorageManager = mockk()
    private lateinit var useCase: SaveVaultImageUseCase

    @Before
    fun setUp() {
        useCase = SaveVaultImageUseCase(vaultImageStorageManager)
    }

    @Test
    fun invoke_returnsFile_whenManagerSucceeds() {
        // Given
        val targetWord = "Apple"
        val language = "Spanish"
        val inputFile = mockk<File>()
        val outputFile = mockk<File>()
        every { vaultImageStorageManager.saveVaultImage(targetWord, language, inputFile) } returns outputFile

        // When
        val result = useCase(targetWord, language, inputFile)

        // Then
        assertEquals(outputFile, result)
        verify(exactly = 1) { vaultImageStorageManager.saveVaultImage(targetWord, language, inputFile) }
    }

    @Test
    fun invoke_returnsNull_whenManagerFails() {
        // Given
        val targetWord = "Apple"
        val language = "Spanish"
        val inputFile = mockk<File>()
        every { vaultImageStorageManager.saveVaultImage(targetWord, language, inputFile) } returns null

        // When
        val result = useCase(targetWord, language, inputFile)

        // Then
        assertEquals(null, result)
        verify(exactly = 1) { vaultImageStorageManager.saveVaultImage(targetWord, language, inputFile) }
    }
}
