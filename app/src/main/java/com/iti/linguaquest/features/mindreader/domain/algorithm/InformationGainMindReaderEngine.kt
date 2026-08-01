package com.iti.linguaquest.features.mindreader.domain.algorithm

import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAttribute
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderCandidateScore
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameHistory
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameConfig
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameState
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGuessResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderHistoryEntry
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderQuestionCandidate
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderResult
import kotlin.math.ln

class InformationGainMindReaderEngine(
    private val attributes: List<MindReaderAttribute>,
    private val config: MindReaderGameConfig
) {
    private val attributesById = attributes.associateBy { it.id }

    fun createSession(
        entities: List<MindReaderEntity>,
        worldKey: String? = null
    ): MindReaderGameState {

        val filtered = entities.filter { worldKey == null || it.worldKey == worldKey }

        require(filtered.isNotEmpty()) { "Mind Reader session requires at least one entity." }

        val initialWeight = 1.0 / filtered.size.toDouble()

        return MindReaderGameState(
            worldKey = worldKey,
            candidates = filtered.map { MindReaderCandidateScore(it, initialWeight) }
        )
    }

    fun nextQuestion(state: MindReaderGameState): MindReaderQuestionCandidate? {
        if (state.candidates.size <= 1) return null

        val remainingAttributes = attributes.filterNot { it.id in state.askedAttributes }.shuffled()

        return remainingAttributes
            .mapNotNull { attribute ->

                val gain = informationGain(state.candidates, attribute.id)
                if (gain <= 0.0) return@mapNotNull null

                MindReaderQuestionCandidate(
                    attributeId = attribute.id,
                    question = attribute.question,
                    informationGain = gain
                )
            }
            .maxByOrNull { it.informationGain }
    }

    fun applyAnswer(
        state: MindReaderGameState,
        attributeId: String,
        answer: MindReaderAnswerOption
    ): MindReaderGameState {
        val attribute = attributesById[attributeId]
            ?: error("Unknown attribute: $attributeId")

        val updatedCandidates = state.candidates.map { candidate ->
            val hasAttribute = candidate.entity.hasAttribute(attributeId)
            val likelihood = answer.likelihood(hasAttribute)
            candidate.copy(weight = candidate.weight * likelihood)
        }

        val normalizedCandidates = normalize(updatedCandidates)
        val updatedState = state.copy(
            candidates = normalizedCandidates,
            askedAttributes = state.askedAttributes + attributeId,
            questionCount = state.questionCount + 1
        )

        return updatedState.copy(
            history = state.history.append(
                MindReaderHistoryEntry(
                    attributeId = attributeId,
                    question = attribute.question,
                    answer = answer,
                    confidenceAfterAnswer = guessProbability(updatedState)
                )
            )
        )
    }

    fun shouldRevealGuess(state: MindReaderGameState): Boolean {
        return state.candidates.size <= 1 ||
            state.questionCount >= config.maxQuestions ||
            guessProbability(state) >= config.guessThreshold ||
            nextQuestion(state) == null
    }

    fun currentGuess(state: MindReaderGameState): MindReaderGuessResult? {
        val candidate = state.candidates.maxByOrNull { it.weight } ?: return null
        return MindReaderGuessResult(
            entity = candidate.entity,
            confidence = candidate.weight.coerceIn(0.0, 1.0)
        )
    }

    fun guessProbability(state: MindReaderGameState): Double {
        return currentGuess(state)?.confidence ?: 0.0
    }

    fun rankedAttributes(state: MindReaderGameState): List<MindReaderQuestionCandidate> {
        return attributes
            .filterNot { it.id in state.askedAttributes }
            .shuffled()
            .mapNotNull { attribute ->
                val gain = informationGain(state.candidates, attribute.id)
                if (gain <= 0.0) return@mapNotNull null
                MindReaderQuestionCandidate(
                    attributeId = attribute.id,
                    question = attribute.question,
                    informationGain = gain
                )
            }
            .sortedByDescending { it.informationGain }
    }

    private fun informationGain(
        candidates: List<MindReaderCandidateScore>,
        attributeId: String
    ): Double {
        val totalWeight = candidates.sumOf { it.weight }
        if (totalWeight <= 0.0) return 0.0

        val yesCandidates = candidates.filter { it.entity.hasAttribute(attributeId) }
        val noCandidates = candidates.filterNot { it.entity.hasAttribute(attributeId) }

        val yesWeight = yesCandidates.sumOf { it.weight }
        val noWeight = noCandidates.sumOf { it.weight }
        if (yesWeight <= 0.0 || noWeight <= 0.0) return 0.0

        val currentEntropy = entropy(candidates)
        val expectedEntropy =
            (yesWeight / totalWeight) * entropy(yesCandidates) +
                (noWeight / totalWeight) * entropy(noCandidates)

        return currentEntropy - expectedEntropy
    }

    private fun entropy(candidates: List<MindReaderCandidateScore>): Double {
        val totalWeight = candidates.sumOf { it.weight }
        if (totalWeight <= 0.0) return 0.0

        return candidates.fold(0.0) { acc, candidate ->
            val probability = candidate.weight / totalWeight
            if (probability <= 0.0) acc else acc - probability * log2(probability)
        }
    }

    private fun normalize(candidates: List<MindReaderCandidateScore>): List<MindReaderCandidateScore> {
        val total = candidates.sumOf { it.weight }
        if (total <= 0.0) {
            val fallbackWeight = 1.0 / candidates.size.coerceAtLeast(1).toDouble()
            return candidates.map { it.copy(weight = fallbackWeight) }
        }

        return candidates.map { it.copy(weight = it.weight / total) }
    }

    fun currentResult(state: MindReaderGameState): MindReaderResult {
        val guess = currentGuess(state)
        return when {
            state.questionCount >= config.maxQuestions -> MindReaderResult.Timeout(
                guess = guess,
                history = state.history
            )
            shouldRevealGuess(state) && guess != null -> MindReaderResult.Guessing(guess)
            else -> MindReaderResult.Playing
        }
    }

    fun remainingQuestions(state: MindReaderGameState): Int {
        return (config.maxQuestions - state.questionCount).coerceAtLeast(0)
    }

    private fun log2(value: Double): Double = ln(value) / ln(2.0)
}
