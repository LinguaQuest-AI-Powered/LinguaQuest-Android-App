package com.iti.linguaquest.core.sound

import com.iti.linguaquest.R

enum class AppSound(val resId: Int, val volume: Float = 1.0f) {
    SUCCESS(R.raw.sound_success, 0.8f),
    FAIL(R.raw.sound_fail, 0.6f),
    CAMERA(R.raw.sound_camera, 0.3f),
    DAILY_REWARD(R.raw.sound_happy, 0.5f),
    COIN(R.raw.sound_coin, 0.5f),
    POP(R.raw.sound_pop, 0.5f),
    SWITCH(R.raw.switch_sound, 0.5f),
    AddedMoney(R.raw.coin_added, 0.5f),
    OPEN_MIC(R.raw.open_mic, 0.6f),
    CLOSE_MIC(R.raw.close_mic, 0.6f),

    Notification(R.raw.notification, 0.6f),

    NotificationDisappear(R.raw.notification_disappear, 0.6f),
    GettingWord(R.raw.waiting_word, 0.6f),
    FoundWord(R.raw.getting_word, 0.6f)
}