package com.iti.linguaquest.core.utils

import java.util.Locale
import kotlin.math.pow
import kotlin.math.log10

fun Int.formatCompact(): String {
    if (this < 1000) return this.toString()
    val exp = (log10(this.toDouble()) / 3).toInt()
    val suffix = "KMBTPE"[exp - 1]
    val value = this / 10.0.pow(exp * 3.0)
    
    val formatted = String.format(Locale.US, "%.1f", value)
    return if (formatted.endsWith(".0")) {
        "${formatted.substring(0, formatted.length - 2)}$suffix"
    } else {
        "$formatted$suffix"
    }
}
