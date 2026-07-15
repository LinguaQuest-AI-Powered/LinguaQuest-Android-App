package com.iti.linguaquest.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

object AppTextStyles {

    val AppTitle
        @Composable
        get() = MaterialTheme.typography.headlineMedium

    val ScreenTitle
        @Composable
        get() = MaterialTheme.typography.headlineSmall

    val SectionTitle
        @Composable
        get() = MaterialTheme.typography.titleLarge

    val LessonTitle
        @Composable
        get() = MaterialTheme.typography.titleMedium

    val Question
        @Composable
        get() = MaterialTheme.typography.titleLarge

    val Word
        @Composable
        get() = MaterialTheme.typography.headlineSmall

    val Translation
        @Composable
        get() = MaterialTheme.typography.bodyLarge

    val Definition
        @Composable
        get() = MaterialTheme.typography.bodyMedium

    val Button
        @Composable
        get() = MaterialTheme.typography.titleLarge

    val Caption
        @Composable
        get() = MaterialTheme.typography.bodySmall

    val DialogTitle
        @Composable
        get() = MaterialTheme.typography.titleLarge

    val DialogMessage
        @Composable
        get() = MaterialTheme.typography.bodyMedium

    val Label
        @Composable
        get() = MaterialTheme.typography.labelMedium
}