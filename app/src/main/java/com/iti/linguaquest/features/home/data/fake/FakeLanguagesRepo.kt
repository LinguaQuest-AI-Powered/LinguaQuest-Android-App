package com.iti.linguaquest.features.home.data.fake

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.model.UserLanguage
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import com.iti.linguaquest.features.home.domain.repository.LanguagesRepo
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeLanguagesRepo @Inject constructor() : LanguagesRepo {

    private val allLanguages = mutableListOf(
        LanguageOption(10, "Arabic", "ar", "https://cdn.linguaquest.com/languages/arabic.png", isAdded = false),
        LanguageOption(8, "Chinese", "zh", "https://cdn.linguaquest.com/languages/chinese.png", isAdded = false),
        LanguageOption(1, "English", "en", "https://cdn.linguaquest.com/languages/english.png", isAdded = false),
        LanguageOption(3, "French", "fr", "https://cdn.linguaquest.com/languages/french.png", isAdded = false),
        LanguageOption(4, "German", "de", "https://cdn.linguaquest.com/languages/german.png", isAdded = true),
        LanguageOption(5, "Italian", "it", "https://cdn.linguaquest.com/languages/italian.png", isAdded = false),
        LanguageOption(7, "Japanese", "ja", "https://cdn.linguaquest.com/languages/japanese.png", isAdded = false),
        LanguageOption(9, "Korean", "ko", "https://cdn.linguaquest.com/languages/korean.png", isAdded = false),
        LanguageOption(6, "Portuguese", "pt", "https://cdn.linguaquest.com/languages/portuguese.png", isAdded = false),
        LanguageOption(2, "Spanish", "es", "https://cdn.linguaquest.com/languages/spanish.png", isAdded = false)
    )

    private val userLanguagesProgress = mutableMapOf(
        4 to Pair(12, 65)
    )

    private var activeLanguageId = 4

    override suspend fun getMyLanguages(): LinguaQuestResult<List<UserLanguage>, LinguaQuestDataError> {
        delay(600)
        val myLanguagesList = allLanguages.filter { it.isAdded }.map { opt ->
            val progress = userLanguagesProgress[opt.id] ?: Pair(1, 0)
            UserLanguage(
                id = opt.id,
                name = opt.name,
                code = opt.code,
                imageUrl = opt.imageUrl,
                level = progress.first,
                isActive = opt.id == activeLanguageId,
                progressPercent = progress.second
            )
        }
        return LinguaQuestResult.Success(myLanguagesList)
    }

    override suspend fun getAvailableLanguages(): LinguaQuestResult<List<LanguageOption>, LinguaQuestDataError> {
        delay(600)
        return LinguaQuestResult.Success(allLanguages.toList())
    }

    override suspend fun addLanguages(languageIds: List<Int>): LinguaQuestResult<List<UserLanguage>, LinguaQuestDataError> {
        delay(600)
        languageIds.forEach { id ->
            val index = allLanguages.indexOfFirst { it.id == id }
            if (index != -1) {
                val opt = allLanguages[index]
                allLanguages[index] = opt.copy(isAdded = true)
                if (!userLanguagesProgress.containsKey(id)) {
                    userLanguagesProgress[id] = Pair(1, 0)
                }
            }
        }
        return getMyLanguages()
    }

    override suspend fun setActiveLanguage(languageId: Int): LinguaQuestResult<UserLanguage, LinguaQuestDataError> {
        delay(600)
        val exists = allLanguages.any { it.id == languageId && it.isAdded }
        if (!exists) {
            return LinguaQuestResult.Failure(LinguaQuestDataError.Local.NOT_FOUND)
        }
        activeLanguageId = languageId
        val opt = allLanguages.first { it.id == languageId }
        val progress = userLanguagesProgress[languageId] ?: Pair(1, 0)
        val activeLang = UserLanguage(
            id = opt.id,
            name = opt.name,
            code = opt.code,
            imageUrl = opt.imageUrl,
            level = progress.first,
            isActive = true,
            progressPercent = progress.second
        )
        return LinguaQuestResult.Success(activeLang)
    }
}
