package com.iti.linguaquest.features.voicegame.data.repository

import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.voicegame.data.remote.PronunciationSentenceGeneratorService
import com.iti.linguaquest.features.voicegame.data.remote.VoiceEvaluationService
import com.iti.linguaquest.features.voicegame.domain.model.PronunciationSentence
import com.iti.linguaquest.features.voicegame.domain.model.VoiceEvaluation
import com.iti.linguaquest.features.voicegame.domain.repository.VoiceGameRepository
import kotlinx.coroutines.flow.firstOrNull
import java.util.Locale
import timber.log.Timber
import javax.inject.Inject

class VoiceGameRepositoryImpl @Inject constructor(
    private val service: VoiceEvaluationService,
    private val generatorService: PronunciationSentenceGeneratorService,
    private val userPreferencesRepository: UserPreferencesRepository
) : VoiceGameRepository {

    override suspend fun evaluatePronunciation(
        targetSentence: String,
        targetLanguage: String,
        audioBytes: ByteArray,
        appLanguage: String?
    ): LinguaQuestResult<VoiceEvaluation, LinguaQuestDataError> {
        return try {
            val resolvedAppLanguage = if (!appLanguage.isNullOrBlank()) {
                appLanguage
            } else {
                val nativeName = userPreferencesRepository.nativeLanguageName.firstOrNull()
                val langCode = userPreferencesRepository.appLanguage.firstOrNull()
                resolveAppLanguageName(nativeName, langCode)
            }

            val response = service.evaluatePronunciation(
                targetSentence = targetSentence,
                targetLanguage = targetLanguage,
                audioBytes = audioBytes,
                appLanguage = resolvedAppLanguage
            )
            Timber.d("Voice evaluation response: rating=%d, correct=%s, wrong=%s, advice=%s, transcription=%s",
                response.rating, response.correctWords, response.wrongWords, response.advice, response.transcription)
            val domainModel = VoiceEvaluation(
                rating = response.rating,
                correctWords = response.correctWords,
                wrongWords = response.wrongWords,
                advice = response.advice
            )
            LinguaQuestResult.Success(domainModel)
        } catch (e: Exception) {
            LinguaQuestResult.Failure(
                LinguaQuestDataError.CustomServerMessage(
                    e.message ?: "Voice evaluation failed"
                )
            )
        }
    }

    private fun resolveAppLanguageName(nativeLanguageName: String?, appLanguageCode: String?): String {
        if (!nativeLanguageName.isNullOrBlank()) {
            return nativeLanguageName
        }
        val code = appLanguageCode?.trim()?.lowercase() ?: Locale.getDefault().language
        return when (code) {
            "ar", "arabic" -> "Arabic"
            "en", "english" -> "English"
            "es", "spanish" -> "Spanish"
            "fr", "french" -> "French"
            "de", "german" -> "German"
            "it", "italian" -> "Italian"
            "ja", "japanese" -> "Japanese"
            "zh", "chinese" -> "Chinese"
            "pt", "portuguese" -> "Portuguese"
            "ru", "russian" -> "Russian"
            else -> Locale.forLanguageTag(code).getDisplayLanguage(Locale.ENGLISH).ifBlank { "English" }
        }
    }

    override suspend fun generatePronunciationSentence(
        targetLanguage: String,
        level: String,
        topic: String,
        wordOfTheDay: String?,
        excludeSentences: List<String>
    ): LinguaQuestResult<PronunciationSentence, LinguaQuestDataError> {
        return try {
            val resolvedTopic = if (!wordOfTheDay.isNullOrBlank()) {
                "$topic (must include the word: '$wordOfTheDay')"
            } else {
                topic
            }
            val count = if (excludeSentences.isEmpty()) 1 else (excludeSentences.size + 2)
            val sentences = generatorService.generateSentences(targetLanguage, level, resolvedTopic, count = count)
            val generated = sentences.shuffled().firstOrNull { it.sentence !in excludeSentences }
                ?: sentences.shuffled().firstOrNull()
                ?: throw IllegalStateException("No sentence generated from AI")
            LinguaQuestResult.Success(
                PronunciationSentence(
                    sentence = generated.sentence,
                    difficulty = generated.difficulty,
                    phonetic = generated.phonetic,
                    translation = generated.translation
                )
            )
        } catch (e: Exception) {
            LinguaQuestResult.Failure(
                LinguaQuestDataError.CustomServerMessage(
                    e.message ?: "Failed to generate sentence"
                )
            )
        }
    }
}
