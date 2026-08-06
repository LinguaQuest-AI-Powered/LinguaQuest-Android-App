package com.iti.linguaquest.features.roleplay.domain.prompt

import org.junit.Assert.assertTrue
import org.junit.Test

class PromptFactoryTest {

    @Test
    fun createBossEvaluationPrompt_containsExpectedParametersAndSchema() {
        // Given
        val transcript = "User: Bonjour\nAI: Bonjour, que voulez-vous?"
        val taskObjective = "Order a hot coffee"
        val nativeLanguage = "English"
        val targetLanguage = "French"

        // When
        val prompt = PromptFactory.createBossEvaluationPrompt(
            transcriptText = transcript,
            taskObjective = taskObjective,
            nativeLanguage = nativeLanguage,
            targetLanguage = targetLanguage
        )

        // Then
        assertTrue(prompt.contains("French"))
        assertTrue(prompt.contains("English"))
        assertTrue(prompt.contains("Order a hot coffee"))
        assertTrue(prompt.contains("task_completed"))
        assertTrue(prompt.contains("fluency_score"))
        assertTrue(prompt.contains("grammar_score"))
        assertTrue(prompt.contains("vocabulary_score"))
        assertTrue(prompt.contains("target_language_percentage"))
        assertTrue(prompt.contains(transcript))
    }

    @Test
    fun createLiveSessionPrompt_containsBossAndLanguageConstraints() {
        // Given
        val bossName = "Pierre"
        val roleDescription = "A busy barista"
        val objective = "Order a croissant"
        val targetLanguage = "French"

        // When
        val prompt = PromptFactory.createLiveSessionPrompt(
            bossName = bossName,
            roleDescription = roleDescription,
            objective = objective,
            targetLanguage = targetLanguage
        )

        // Then
        assertTrue(prompt.contains("Pierre"))
        assertTrue(prompt.contains("A busy barista"))
        assertTrue(prompt.contains("Order a croissant"))
        assertTrue(prompt.contains("French"))
        assertTrue(prompt.contains("IMMERSION & LANGUAGE CONSTRAINTS"))
    }
}
