package com.iti.linguaquest.features.lockscreen.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.wallet.domain.repository.WalletRepository
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import javax.inject.Inject

class EnableLockScreenVocabularyUseCase @Inject constructor(
    private val lockScreenRepository: LockScreenRepository,
    private val walletRepository: WalletRepository
) {
    suspend operator fun invoke(
        operationId: String, 
        amount: Int = 50
    ): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return when (val result = walletRepository.adjustWallet(xpDelta = 0, coinsDelta = -amount)) {
            is LinguaQuestResult.Success -> {
                try {
                    lockScreenRepository.enable()
                    LinguaQuestResult.Success(Unit)
                } catch (_: Exception) {
                    LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
                }
            }
            is LinguaQuestResult.Failure -> result
        }
    }
}
