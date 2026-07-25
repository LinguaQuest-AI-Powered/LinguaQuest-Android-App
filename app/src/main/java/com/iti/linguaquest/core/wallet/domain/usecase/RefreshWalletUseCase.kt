package com.iti.linguaquest.core.wallet.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import com.iti.linguaquest.core.wallet.domain.repository.WalletRepository
import javax.inject.Inject

class RefreshWalletUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(): LinguaQuestResult<Wallet, LinguaQuestDataError> {
        return repository.refreshWallet()
    }
}
