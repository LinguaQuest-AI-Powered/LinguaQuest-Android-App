package com.iti.linguaquest.features.game.domain.usecase

import com.iti.linguaquest.core.utils.VaultImageStorageManager
import java.io.File
import javax.inject.Inject

class SaveVaultImageUseCase @Inject constructor(
    private val vaultImageStorageManager: VaultImageStorageManager
) {
    operator fun invoke(targetWord: String, imageFile: File): File? {
        return vaultImageStorageManager.saveVaultImage(targetWord, imageFile)
    }
}
