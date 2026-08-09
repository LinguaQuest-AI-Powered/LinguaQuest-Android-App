package com.iti.linguaquest.features.gallery.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.gallery.domain.usecase.DeleteWordUseCase
import com.iti.linguaquest.features.gallery.domain.usecase.GetWordsWithImagesUseCase
import com.iti.linguaquest.features.gallery.domain.usecase.RefreshGalleryUseCase
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import com.iti.linguaquest.features.lockscreen.domain.usecase.GetLockScreenPostedOrOpenedWordsUseCase
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryEffect
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryIntent
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getWordsWithImagesUseCase: GetWordsWithImagesUseCase,
    private val refreshGalleryUseCase: RefreshGalleryUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
    private val getLockScreenPostedOrOpenedWordsUseCase: GetLockScreenPostedOrOpenedWordsUseCase,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GalleryState())
    val state = _state.asStateFlow()

    private val _effects = Channel<GalleryEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()
    val isOnline = observeNetworkStatusUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

    init {
        observeWords()
        observeLockScreenWords()
        refreshWords()
    }

    fun onIntent(intent: GalleryIntent) {
        when (intent) {
            GalleryIntent.LoadWords -> refreshWords()
            GalleryIntent.RefreshWords -> refreshWords(isPullToRefresh = true)
            is GalleryIntent.CategorySelected -> filterByCategory(intent.category)
            is GalleryIntent.LockScreenCategorySelected -> filterLockScreenByCategory(intent.category)
            is GalleryIntent.DeleteWordClicked -> deleteWord(intent.word)
            is GalleryIntent.WordItemClicked -> navigateToReview(intent.wordId)
            is GalleryIntent.LockScreenWordItemClicked -> navigateToLockScreenReview(intent.wordId)
        }
    }

    private fun observeWords() {
        getWordsWithImagesUseCase()
            .onEach { words ->
                val categories = extractCategories(words)
                val selectedCategory = resolveSelectedCategory(
                    selectedCategory = _state.value.selectedCategory,
                    categories = categories
                )
                val filteredWords = filterWords(words, selectedCategory)

                _state.update { current ->
                    current.copy(
                        isLoading = false,
                        words = words,
                        filteredWords = filteredWords,
                        categories = categories,
                        selectedCategory = selectedCategory,
                        errorMessage = if (words.isNotEmpty()) null else current.errorMessage
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeLockScreenWords() {
        getLockScreenPostedOrOpenedWordsUseCase()
            .onEach { words ->
                val categories = extractLockScreenCategories(words)
                val selectedCategory = resolveSelectedCategory(
                    selectedCategory = _state.value.selectedLockScreenCategory,
                    categories = categories
                )
                val filteredWords = filterLockScreenWords(words, selectedCategory)

                _state.update { current ->
                    current.copy(
                        lockScreenWords = words,
                        filteredLockScreenWords = filteredWords,
                        lockScreenCategories = categories,
                        selectedLockScreenCategory = selectedCategory
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun filterByCategory(category: String) {
        _state.update {
            it.copy(
                selectedCategory = category,
                filteredWords = filterWords(it.words, category)
            )
        }
    }

    private fun filterLockScreenByCategory(category: String) {
        _state.update {
            it.copy(
                selectedLockScreenCategory = category,
                filteredLockScreenWords = filterLockScreenWords(it.lockScreenWords, category)
            )
        }
    }

    private fun refreshWords(isPullToRefresh: Boolean = false) {
        viewModelScope.launch {
            val hasCache = _state.value.words.isNotEmpty()
            _state.update {
                it.copy(
                    isRefreshing = isPullToRefresh,
                    errorMessage = if (hasCache) null else it.errorMessage
                )
            }

            when (val result = refreshGalleryUseCase()) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(isLoading = false, isRefreshing = false, errorMessage = null) }
                }

                is LinguaQuestResult.Failure -> {
                    val dataError = result.error as? LinguaQuestDataError
                    val stillHasCache = _state.value.words.isNotEmpty()
                    val isOfflineError = dataError == LinguaQuestDataError.Remote.NO_INTERNET
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = if (stillHasCache || isOfflineError) null else dataError?.toUiText()
                        )
                    }

                    if (isPullToRefresh && isOfflineError) {
                        sendEffect(
                            GalleryEffect.ShowError(
                                title = UiText.StringResource(R.string.offline_title),
                                message = UiText.StringResource(R.string.offline_msg),
                                type = SnackbarType.INFO,
                                retryable = false
                            )
                        )
                    } else if (!isOfflineError && !stillHasCache) {
                        val errorUiText = dataError?.toUiText() ?: UiText.StringResource(R.string.error_generic)
                        sendEffect(
                            GalleryEffect.ShowError(
                                message = errorUiText,
                                type = SnackbarType.ERROR,
                                retryable = true
                            )
                        )
                    }
                }
            }
        }
    }



    private fun deleteWord(word: WordEntity) {
        viewModelScope.launch {
            deleteWordUseCase(word)
        }
    }

    private fun navigateToReview(wordId: Int) {
        viewModelScope.launch {
            val word = _state.value.words.find { it.id == wordId } ?: return@launch
            _effects.send(GalleryEffect.NavigateToReview(word))
        }
    }

    private fun navigateToLockScreenReview(wordId: Int) {
        viewModelScope.launch {
            val lockScreenWord = _state.value.lockScreenWords.find { it.id == wordId } ?: return@launch
            _effects.send(GalleryEffect.NavigateToReview(lockScreenWord.toReviewWordEntity()))
        }
    }

    private fun extractCategories(words: List<WordEntity>): List<String> {
        val uniqueCategories = words.map { it.category }.distinct().filter { it.isNotBlank() }
        return listOf(ALL_ITEMS_CATEGORY) + uniqueCategories
    }

    private fun resolveSelectedCategory(
        selectedCategory: String,
        categories: List<String>
    ): String {
        return if (categories.contains(selectedCategory)) selectedCategory else ALL_ITEMS_CATEGORY
    }

    private fun filterWords(words: List<WordEntity>, category: String): List<WordEntity> {
        return if (category == ALL_ITEMS_CATEGORY) {
            words
        } else {
            words.filter { it.category.equals(category, ignoreCase = true) }
        }
    }

    private fun extractLockScreenCategories(words: List<LockScreenWord>): List<String> {
        val uniqueCategories = words
            .map { it.proficiencyLevel }
            .distinct()
            .filter { it.isNotBlank() }
        return listOf(ALL_ITEMS_CATEGORY) + uniqueCategories
    }

    private fun filterLockScreenWords(words: List<LockScreenWord>, category: String): List<LockScreenWord> {
        return if (category == ALL_ITEMS_CATEGORY) {
            words
        } else {
            words.filter { it.proficiencyLevel.equals(category, ignoreCase = true) }
        }
    }

    private fun sendEffect(effect: GalleryEffect) {
        viewModelScope.launch { _effects.send(effect) }
    }


    companion object {
        const val ALL_ITEMS_CATEGORY = "All Items"
    }
}

private fun LockScreenWord.toReviewWordEntity(): WordEntity {
    return WordEntity(
        id = id,
        sourceWord = word,
        translatedWord = translation,
        sourceLanguage = targetLanguage,
        targetLanguage = nativeLanguage,
        category = proficiencyLevel,
        imagePath = "android.resource://com.iti.linguaquest/${R.drawable.lingo_searching}"
    )
}
