package com.iti.linguaquest.core.wallet.data.datasource.local

import androidx.datastore.preferences.core.intPreferencesKey

object WalletKeys {
    val XP = intPreferencesKey("xp")
    val COINS = intPreferencesKey("coins")
}
