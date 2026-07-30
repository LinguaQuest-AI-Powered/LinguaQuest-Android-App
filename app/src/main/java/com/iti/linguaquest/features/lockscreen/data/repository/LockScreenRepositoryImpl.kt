package com.iti.linguaquest.features.lockscreen.data.repository

import com.iti.linguaquest.core.database.lockscreen.LockScreenWordStatus
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.lockscreen.data.local.LockScreenLocalDataSource
import com.iti.linguaquest.features.lockscreen.data.mapper.toDomain
import com.iti.linguaquest.features.lockscreen.data.mapper.toEntity
import com.iti.linguaquest.features.lockscreen.data.remote.LockScreenRemoteDataSource
import com.iti.linguaquest.features.lockscreen.domain.model.GeneratedVocabularyWord
import com.iti.linguaquest.features.lockscreen.domain.model.VocabularyBatchParams
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenFeatureMetadata
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LockScreenRepositoryImpl @Inject constructor(
    private val remoteDataSource: LockScreenRemoteDataSource,
    private val localDataSource: LockScreenLocalDataSource
) : LockScreenRepository {

    override val featureEnabled: Flow<Boolean> = localDataSource.featureEnabled
    override val pendingGeneration: Flow<Boolean> = localDataSource.pendingGeneration
    override val batchSize: Flow<Int> = localDataSource.batchSize
    override val lastGenerationTime: Flow<Long?> = localDataSource.lastGenerationTime
    override val lastNativeLanguage: Flow<String?> = localDataSource.lastNativeLanguage
    override val lastTargetLanguage: Flow<String?> = localDataSource.lastTargetLanguage
    override val lastProficiencyLevel: Flow<String?> = localDataSource.lastProficiencyLevel
    override val pendingOperationId: Flow<String?> = localDataSource.pendingOperationId
    override val lastRewardedMilestoneCount: Flow<Int?> = localDataSource.lastRewardedMilestoneCount
    override val pendingCount: Flow<Int> = localDataSource.pendingCount
    override val allWords: Flow<List<LockScreenWord>> = localDataSource.allWords().map { list -> list.map { it.toDomain() } }
    override val pendingWord: Flow<LockScreenWord?> = localDataSource.pendingWord().map { it?.toDomain() }
    override val postedOrOpenedWords: Flow<List<LockScreenWord>> = localDataSource.postedOrOpenedWords().map { list -> list.map { it.toDomain() } }

    override suspend fun enable() {
        localDataSource.saveFeatureEnabled(true)
        localDataSource.savePendingGeneration(true)
    }

    override suspend fun disable() {
        localDataSource.clearAll()
        localDataSource.clearFeatureMetadata()
    }

    override suspend fun generateBatch(
        params: VocabularyBatchParams
    ): LinguaQuestResult<List<GeneratedVocabularyWord>, LinguaQuestDataError> {
        return remoteDataSource.generateVocabulary(params)
    }

    override suspend fun saveGeneratedBatch(
        words: List<GeneratedVocabularyWord>,
        params: VocabularyBatchParams
    ): LinguaQuestResult<Int, LinguaQuestDataError> {
        val entities = words.distinctBy { it.word.lowercase() }
            .map { it.toEntity(params.nativeLanguage, params.targetLanguage, params.proficiencyLevel) }
        return try {
            localDataSource.insertBatch(entities)
            localDataSource.saveFeatureEnabled(true)
            localDataSource.savePendingGeneration(false)
            localDataSource.saveLastGenerationTime(System.currentTimeMillis())
            localDataSource.saveLastNativeLanguage(params.nativeLanguage)
            localDataSource.saveLastTargetLanguage(params.targetLanguage)
            localDataSource.saveLastProficiencyLevel(params.proficiencyLevel)
            LinguaQuestResult.Success(entities.size)
        } catch (e: Exception) {
             LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
        }
    }

    override suspend fun getWordById(wordId: Int): LockScreenWord? {
        return localDataSource.getWord(wordId)?.toDomain()
    }

    override suspend fun markPosted(wordId: Int): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            localDataSource.updateStatus(
                wordId = wordId,
                status = LockScreenWordStatus.POSTED.name,
                postedAt = System.currentTimeMillis()
            )
            LinguaQuestResult.Success(Unit)
        } catch (e: Exception) {
             LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
        }
    }

    override suspend fun markFailed(wordId: Int): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            localDataSource.updateStatus(
                wordId = wordId,
                status = LockScreenWordStatus.FAILED.name
            )
            LinguaQuestResult.Success(Unit)
        } catch (e: Exception) {
             LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
        }
    }

    override suspend fun markOpened(wordId: Int): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            localDataSource.updateStatus(
                wordId = wordId,
                status = LockScreenWordStatus.OPENED.name,
                openedAt = System.currentTimeMillis()
            )
            LinguaQuestResult.Success(Unit)
        } catch (e: Exception) {
             LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
        }
    }

    override suspend fun clearWords(): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            localDataSource.clearAll()
            LinguaQuestResult.Success(Unit)
        } catch (e: Exception) {
             LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
        }
    }

    override suspend fun clearWordsForLanguage(targetLanguage: String): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            localDataSource.clearByTargetLanguage(targetLanguage)
            LinguaQuestResult.Success(Unit)
        } catch (e: Exception) {
             LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
        }
    }

    override suspend fun clearWordsForLanguageAndLevel(
        targetLanguage: String,
        proficiencyLevel: String
    ): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            localDataSource.clearByTargetLanguageAndLevel(targetLanguage, proficiencyLevel)
            LinguaQuestResult.Success(Unit)
        } catch (e: Exception) {
             LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
        }
    }

    override suspend fun updateFeatureMetadata(metadata: LockScreenFeatureMetadata) {
        localDataSource.saveFeatureEnabled(metadata.enabled)
        localDataSource.savePendingGeneration(metadata.pendingGeneration)
        localDataSource.savePendingOperationId(metadata.operationId)
        if (metadata.batchSize != null) localDataSource.saveBatchSize(metadata.batchSize)
        localDataSource.saveLastGenerationTime(metadata.lastGenerationTime)
        localDataSource.saveLastNativeLanguage(metadata.lastNativeLanguage)
        localDataSource.saveLastTargetLanguage(metadata.lastTargetLanguage)
        localDataSource.saveLastProficiencyLevel(metadata.lastProficiencyLevel)
    }

    override suspend fun saveLastRewardedMilestoneCount(count: Int?) {
        localDataSource.saveLastRewardedMilestoneCount(count)
    }

    override suspend fun recentGeneratedWords(limit: Int): List<String> {
        return localDataSource.getRecentWords(limit)
    }

    override suspend fun pendingCountOnce(): Int {
        return localDataSource.pendingCountOnce()
    }

    override suspend fun observePendingOnce(): LockScreenWord? {
        return localDataSource.getRandomPendingWordOnce()?.toDomain()
    }

    private fun buildFallbackVocabulary(
        targetLanguage: String,
        batchSize: Int
    ): List<GeneratedVocabularyWord> {
        val words = when (targetLanguage.lowercase()) {
            "spanish" -> spanishFallbackWords()
            "french" -> frenchFallbackWords()
            "german" -> germanFallbackWords()
            "japanese" -> japaneseFallbackWords()
            else -> spanishFallbackWords()
        }
        return words.take(batchSize)
    }

    private fun spanishFallbackWords(): List<GeneratedVocabularyWord> = listOf(
        GeneratedVocabularyWord("casa", "house", "Veo una casa azul."),
        GeneratedVocabularyWord("libro", "book", "Leo un libro tranquilo."),
        GeneratedVocabularyWord("agua", "water", "Bebo agua fria."),
        GeneratedVocabularyWord("comer", "to eat", "Quiero comer ahora."),
        GeneratedVocabularyWord("hablar", "to speak", "Mi amigo habla despacio."),
        GeneratedVocabularyWord("perro", "dog", "El perro corre rapido."),
        GeneratedVocabularyWord("mesa", "table", "La mesa esta limpia."),
        GeneratedVocabularyWord("luz", "light", "La luz es suave."),
        GeneratedVocabularyWord("amigo", "friend", "Mi amigo sonríe hoy."),
        GeneratedVocabularyWord("escuela", "school", "La escuela esta abierta.")
    )

    private fun frenchFallbackWords(): List<GeneratedVocabularyWord> = listOf(
        GeneratedVocabularyWord("maison", "house", "Je vois une maison bleue."),
        GeneratedVocabularyWord("livre", "book", "Je lis un livre calme."),
        GeneratedVocabularyWord("eau", "water", "Je bois de l'eau froide."),
        GeneratedVocabularyWord("manger", "to eat", "Je veux manger maintenant."),
        GeneratedVocabularyWord("parler", "to speak", "Mon ami parle doucement."),
        GeneratedVocabularyWord("chien", "dog", "Le chien court vite."),
        GeneratedVocabularyWord("table", "table", "La table est propre."),
        GeneratedVocabularyWord("lumiere", "light", "La lumiere est douce."),
        GeneratedVocabularyWord("ami", "friend", "Mon ami sourit aujourd'hui."),
        GeneratedVocabularyWord("ecole", "school", "L'ecole est ouverte.")
    )

    private fun germanFallbackWords(): List<GeneratedVocabularyWord> = listOf(
        GeneratedVocabularyWord("Haus", "house", "Ich sehe ein blaues Haus."),
        GeneratedVocabularyWord("Buch", "book", "Ich lese ein ruhiges Buch."),
        GeneratedVocabularyWord("Wasser", "water", "Ich trinke kaltes Wasser."),
        GeneratedVocabularyWord("essen", "to eat", "Ich will jetzt essen."),
        GeneratedVocabularyWord("sprechen", "to speak", "Mein Freund spricht langsam."),
        GeneratedVocabularyWord("Hund", "dog", "Der Hund rennt schnell."),
        GeneratedVocabularyWord("Tisch", "table", "Der Tisch ist sauber."),
        GeneratedVocabularyWord("Licht", "light", "Das Licht ist weich."),
        GeneratedVocabularyWord("Freund", "friend", "Mein Freund lachelt heute."),
        GeneratedVocabularyWord("Schule", "school", "Die Schule ist offen.")
    )

    private fun japaneseFallbackWords(): List<GeneratedVocabularyWord> = listOf(
        GeneratedVocabularyWord("いえ", "house", "あおい いえ を みます。"),
        GeneratedVocabularyWord("ほん", "book", "しずかな ほん を よみます。"),
        GeneratedVocabularyWord("みず", "water", "つめたい みず を のみます。"),
        GeneratedVocabularyWord("たべる", "to eat", "いま たべたい です。"),
        GeneratedVocabularyWord("はなす", "to speak", "ともだち は ゆっくり はなします。"),
        GeneratedVocabularyWord("いぬ", "dog", "いぬ が はやく はしります。"),
        GeneratedVocabularyWord("つくえ", "table", "つくえ は きれい です。"),
        GeneratedVocabularyWord("ひかり", "light", "ひかり は やさしい です。"),
        GeneratedVocabularyWord("ともだち", "friend", "ともだち は きょう ほほえみます。"),
        GeneratedVocabularyWord("がっこう", "school", "がっこう は あいています。")
    )
}
