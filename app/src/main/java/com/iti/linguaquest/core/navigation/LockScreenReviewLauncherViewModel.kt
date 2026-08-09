package com.iti.linguaquest.core.navigation

import androidx.lifecycle.ViewModel
import com.iti.linguaquest.R
import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import com.iti.linguaquest.features.lockscreen.domain.usecase.GetLockScreenWordByIdUseCase
import com.iti.linguaquest.features.lockscreen.domain.usecase.MarkLockScreenWordOpenedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LockScreenReviewLauncherViewModel @Inject constructor(
    private val getLockScreenWordByIdUseCase: GetLockScreenWordByIdUseCase,
    private val markLockScreenWordOpenedUseCase: MarkLockScreenWordOpenedUseCase
) : ViewModel() {

    suspend fun prepareReviewWord(wordId: Int): WordEntity? {
        val word = getLockScreenWordByIdUseCase(wordId) ?: return null
        markLockScreenWordOpenedUseCase(wordId)
        return word.toReviewWordEntity()
    }

    private fun LockScreenWord.toReviewWordEntity(): WordEntity {
        return WordEntity(
            id = id,
            sourceWord = word,
            translatedWord = translation,
            sourceLanguage = targetLanguage,
            targetLanguage = nativeLanguage,
            category = difficulty.ifBlank { proficiencyLevel },
            imagePath = "android.resource://com.iti.linguaquest/${R.drawable.lingo_searching}"
        )
    }
}
