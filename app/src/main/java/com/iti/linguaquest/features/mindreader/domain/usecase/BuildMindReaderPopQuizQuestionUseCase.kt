package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.LocalizedText
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderPopQuizChoice
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderPopQuizQuestion
import com.iti.linguaquest.features.mindreader.domain.repository.MindReaderRepository
import java.util.UUID
import javax.inject.Inject

class BuildMindReaderPopQuizQuestionUseCase @Inject constructor(
    private val repository: MindReaderRepository
) {

    suspend operator fun invoke(
        categoryContext: String,
        targetLanguage: String,
        nativeLanguage: String,
        correctEntity: MindReaderEntity
    ): MindReaderPopQuizQuestion? {
        
        val correctWordTarget = correctEntity.translations.resolve(targetLanguage)

        val aiResponse = repository.generateQuizChoices(
            categoryContext = categoryContext,
            correctWord = correctWordTarget,
            nativeLanguage = nativeLanguage,
            targetLanguage = targetLanguage
        ) ?: return null

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

        val choices = aiResponse.map { choiceDto ->
            if (choiceDto.isCorrect) {
                MindReaderPopQuizChoice(entity = correctEntity)
            } else {
                val dummyEntity = MindReaderEntity(
                    id = UUID.randomUUID().toString(),
                    worldKey = categoryContext,
                    translations = LocalizedText(
                        mapOf(
                            targetLanguage to choiceDto.translationText,
                            nativeLanguage to ""
                        )
                    ),
                    emoji = "🤔",
                    positiveAttributes = emptySet()
                )
                MindReaderPopQuizChoice(entity = dummyEntity)
            }
        }.shuffled()

        return MindReaderPopQuizQuestion(
            prompt = prompt,
            correctEntity = correctEntity,
            choices = choices
        )
    }
}
