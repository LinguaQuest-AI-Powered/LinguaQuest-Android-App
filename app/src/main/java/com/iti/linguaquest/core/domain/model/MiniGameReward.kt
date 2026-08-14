package com.iti.linguaquest.core.domain.model

enum class MiniGameReward(val xp: Int, val coins: Int) {
    VOICE_GAME(xp = 5, coins = 1),
    ROLEPLAY_3_STARS(xp = 15, coins = 3),
    ROLEPLAY_2_STARS(xp = 10, coins = 2),
    ROLEPLAY_1_STAR(xp = 5, coins = 1),
    MIND_READER_VICTORY(xp = 10, coins = 2),
    MIND_READER_STUMP(xp = 15, coins = 3)
}
