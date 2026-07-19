package com.iti.linguaquest.core.sound

import com.iti.linguaquest.R

enum class AppSound(val resId: Int, val volume: Float = 1.0f) {
    SUCCESS(R.raw.sound_success, 0.6f),
    FAIL(R.raw.sound_fail, 0.5f),
    CAMERA(R.raw.sound_camera, 0.3f),
    DAILY_REWARD(R.raw.sound_happy, 0.4f),
    COIN(R.raw.sound_coin, 0.5f),
    POP(R.raw.sound_pop, 0.5f)
}