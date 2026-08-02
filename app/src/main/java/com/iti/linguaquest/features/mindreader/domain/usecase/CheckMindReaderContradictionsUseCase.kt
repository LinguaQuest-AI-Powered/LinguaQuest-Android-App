package com.iti.linguaquest.features.mindreader.domain.usecase

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAttribute
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderContradictionDetail
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderContradictionResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameHistory
import javax.inject.Inject

class CheckMindReaderContradictionsUseCase @Inject constructor() {

    operator fun invoke(
        history: MindReaderGameHistory,
        evaluatedEntity: MindReaderEntity,
        attributes: List<MindReaderAttribute>
    ): MindReaderContradictionResult {
        val attributesById = attributes.associateBy { it.id }
        val details = history.turns.map { turn ->
            val attribute = attributesById[turn.attributeId]
                ?: error("Unknown attribute in history: ${turn.attributeId}")
            val actualHasAttribute = evaluatedEntity.hasAttribute(turn.attributeId)
            val isContradiction = turn.answer.contradicts(actualHasAttribute)

            MindReaderContradictionDetail(
                attributeId = turn.attributeId,
                question = attribute.question,
                answer = turn.answer,
                actualHasAttribute = actualHasAttribute,
                isContradiction = isContradiction
            )
        }

        val contradictionCount = details.count { it.isContradiction }
        val totalCount = details.size
        val matchedCount = totalCount - contradictionCount

        return MindReaderContradictionResult(
            evaluatedEntity = evaluatedEntity,
            details = details,
            contradictionCount = contradictionCount,
            matchedCount = matchedCount,
            totalCount = totalCount
        )
    }
}
