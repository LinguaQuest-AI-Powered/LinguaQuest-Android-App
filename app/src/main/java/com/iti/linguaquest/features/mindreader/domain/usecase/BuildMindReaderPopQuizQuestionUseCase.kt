package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.LocalizedText
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderPopQuizChoice
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderPopQuizQuestion
import javax.inject.Inject

class BuildMindReaderPopQuizQuestionUseCase @Inject constructor() {

    operator fun invoke(
        correctEntity: MindReaderEntity,
        candidatePool: List<MindReaderEntity>
    ): MindReaderPopQuizQuestion {
        val pool = candidatePool.distinctBy { it.id }
        require(pool.size >= 3) {
            "Pop quiz requires at least 3 unique entities."
        }

        val distractors = pool
            .asSequence()
            .filterNot { it.id == correctEntity.id }
            .sortedBy { it.id }
            .take(2)
            .toList()

        require(distractors.size == 2) {
            "Pop quiz requires at least 2 distractors."
        }

        val prompt = LocalizedText(
            mapOf(
                "ar" to "اختر الترجمة الصحيحة لتحصل على المكافأة.",
                "en" to "Tap the correct translation to claim your reward.",
                "es" to "Toca la traducción correcta para reclamar tu recompensa.",
                "de" to "Tippe auf die richtige Übersetzung, um deine Belohnung zu erhalten.",
                "it" to "Tocca la traduzione corretta per ottenere la tua ricompensa.",
                "fr" to "Touchez la bonne traduction pour obtenir votre récompense.",
                "pt" to "Toque na tradução correta para ganhar sua recompensa."
            )
        )

        val orderedChoices = buildList {
            add(correctEntity)
            addAll(distractors)
        }.sortedBy { it.id }

        return MindReaderPopQuizQuestion(
            prompt = prompt,
            correctEntity = correctEntity,
            choices = orderedChoices.map { MindReaderPopQuizChoice(entity = it) }
        )
    }
}
