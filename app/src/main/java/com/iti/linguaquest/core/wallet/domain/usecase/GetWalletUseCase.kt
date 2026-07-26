package com.iti.linguaquest.core.wallet.domain.usecase

import com.iti.linguaquest.core.wallet.domain.model.Wallet
import com.iti.linguaquest.core.wallet.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWalletUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    operator fun invoke(): Flow<Wallet> = repository.wallet
}
