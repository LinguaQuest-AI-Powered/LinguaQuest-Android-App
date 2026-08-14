package com.iti.linguaquest.core.domain.model

enum class GameCost(val coins: Int) {
    HINT(coins = 30),
    CHANGE_WORD(coins = 50),
    MIND_READER_TRANSLATION(coins = 5)
}
