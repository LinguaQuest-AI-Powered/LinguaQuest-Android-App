package com.iti.linguaquest.core.wallet.data.datasource.remote.dto

data class WalletDto(
    val xp: Int,
    val coins: Int,
    val xpDelta: Int? = null,
    val coinsDelta: Int? = null
)
