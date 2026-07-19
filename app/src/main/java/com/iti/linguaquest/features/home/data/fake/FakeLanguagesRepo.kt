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
        LanguageOption(1, "Spanish", "es", "/media/languages/spanish.png", isAdded = true),
        LanguageOption(2, "French", "fr", "/media/languages/french.png", isAdded = true),
        LanguageOption(3, "Japanese", "ja", "/media/languages/japanese.png", isAdded = true),
        LanguageOption(4, "German", "de", "/media/languages/german.png", isAdded = false),
        LanguageOption(5, "Italian", "it", "/media/languages/italian.png", isAdded = false),
        LanguageOption(6, "Korean", "ko", "/media/languages/korean.png", isAdded = false),
        LanguageOption(7, "Portuguese", "pt", "/media/languages/portuguese.png", isAdded = false)
    )

    private val userLanguagesProgress = mutableMapOf(
        1 to Pair(12, 65),
        2 to Pair(4, 20),
        3 to Pair(1, 0)
    )

    private var activeLanguageId = 1

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
