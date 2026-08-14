package com.iti.linguaquest.features.mindreader.domain.usecase

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

        val correctWordTarget = correctEntity.targetText

        val aiResponse = repository.generateQuizChoices(
            categoryContext = categoryContext,
            correctWord = correctWordTarget,
            nativeLanguage = nativeLanguage,
            targetLanguage = targetLanguage
        ) ?: return null

        val choices = aiResponse.map { choiceDto ->
            if (choiceDto.isCorrect) {
                MindReaderPopQuizChoice(entity = correctEntity)
            } else {
                val dummyEntity = MindReaderEntity(
                    id = UUID.randomUUID().toString(),
                    worldKey = categoryContext,
                    targetText = choiceDto.translationText,
                    nativeText = "",
                    emoji = "🤔"
                )
                MindReaderPopQuizChoice(entity = dummyEntity)
            }
        }.shuffled()

        return MindReaderPopQuizQuestion(
            correctEntity = correctEntity,
            choices = choices
        )
    }
}
