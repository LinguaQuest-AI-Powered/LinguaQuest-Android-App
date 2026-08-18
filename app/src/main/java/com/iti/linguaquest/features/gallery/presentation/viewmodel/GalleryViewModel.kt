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
import com.iti.linguaquest.core.sharedComponents.state.DataStatus
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryEffect
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryIntent
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryState
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
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
import com.iti.linguaquest.core.session.SessionEvent
import com.iti.linguaquest.core.session.SessionEventBus
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getWordsWithImagesUseCase: GetWordsWithImagesUseCase,
    private val refreshGalleryUseCase: RefreshGalleryUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
    private val getLockScreenPostedOrOpenedWordsUseCase: GetLockScreenPostedOrOpenedWordsUseCase,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    private val snackbarController: SnackbarController,
    private val sessionEventBus: SessionEventBus
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
        observeSessionEvents()
        refreshWords()
    }

    private fun observeSessionEvents() {
        viewModelScope.launch {
            sessionEventBus.events.collect { event ->
                when (event) {
                    is SessionEvent.LevelCompleted -> refreshWords()
                    else -> Unit
                }
            }
        }
    }

    fun onIntent(intent: GalleryIntent) {
        when (intent) {
            GalleryIntent.LoadWords -> refreshWords()
            GalleryIntent.RefreshWords -> refreshWords(isPullToRefresh = true)
            is GalleryIntent.CategorySelected -> filterByCategory(intent.category)
            is GalleryIntent.LockScreenCategorySelected -> filterLockScreenByCategory(intent.category)
            is GalleryIntent.DeleteWordClicked -> deleteWord(intent.wordId)
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
                        dataStatus = if (words.isNotEmpty() && current.dataStatus is DataStatus.Loading) DataStatus.Loaded else current.dataStatus,
                        words = words,
                        filteredWords = filteredWords,
                        categories = categories,
                        selectedCategory = selectedCategory
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
                    dataStatus = if (isPullToRefresh) DataStatus.Refreshing else if (hasCache) DataStatus.Loaded else DataStatus.Loading
                )
            }

            when (val result = refreshGalleryUseCase()) {
                is LinguaQuestResult.Success -> {
                    _state.update { it.copy(dataStatus = DataStatus.Loaded) }
                }

                is LinguaQuestResult.Failure -> {
                    val dataError = result.error as? LinguaQuestDataError
                    val stillHasCache = _state.value.words.isNotEmpty()
                    val isOfflineError = dataError == LinguaQuestDataError.Remote.NO_INTERNET
                    val errorUiText = dataError?.toUiText() ?: UiText.StringResource(R.string.error_generic)

                    if (!stillHasCache) {
                        _state.update {
                            it.copy(dataStatus = DataStatus.Error(errorUiText))
                        }
                    } else {
                        _state.update {
                            it.copy(dataStatus = DataStatus.Loaded)
                        }
                        if (isOfflineError) {
                            snackbarController.sendEvent(
                                SnackbarEvent(
                                    title = UiText.StringResource(R.string.offline_title),
                                    message = UiText.StringResource(R.string.offline_msg),
                                    type = SnackbarType.INFO
                                )
                            )
                        } else {
                            snackbarController.sendEvent(
                                SnackbarEvent(
                                    message = errorUiText,
                                    type = SnackbarType.ERROR,
                                    actionLabel = UiText.StringResource(R.string.retry),
                                    onAction = { refreshWords(isPullToRefresh = true) }
                                )
                            )
                        }
                    }
                }
            }
        }
    }



    private fun deleteWord(wordId: Int) {
        viewModelScope.launch {
            deleteWordUseCase(wordId)
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
            _effects.send(GalleryEffect.ShowLockScreenWordDialog(wordId))
        }
    }

    private fun extractCategories(words: List<WordEntity>): List<String> {
        val uniqueCategories = words.map { it.category }.distinct().sorted()
        return listOf(ALL_ITEMS_CATEGORY) + uniqueCategories
    }

    private fun resolveSelectedCategory(
        selectedCategory: String,
        categories: List<String>
    ): String {
        return if (categories.contains(selectedCategory)) selectedCategory else ALL_ITEMS_CATEGORY
    }

    private fun filterWords(words: List<WordEntity>, category: String): List<WordEntity> {
        val reversedWords = words.reversed()
        return if (category == ALL_ITEMS_CATEGORY) {
            reversedWords
        } else {
            reversedWords.filter { 
                it.category.equals(category, ignoreCase = true) 
            }
        }
    }

    private fun extractLockScreenCategories(words: List<LockScreenWord>): List<String> {
        return listOf(ALL_ITEMS_CATEGORY, "Easy", "Medium", "Hard")
    }

    private fun filterLockScreenWords(words: List<LockScreenWord>, category: String): List<LockScreenWord> {
        return if (category == ALL_ITEMS_CATEGORY) {
            words
        } else {
            words.filter { 
                mapRawCategoryToBucket(it.difficulty).equals(category, ignoreCase = true) 
            }
        }
    }

    private fun mapRawCategoryToBucket(rawCategory: String): String {
        return when (rawCategory.trim().lowercase()) {
            "beginner", "easy", "سهل", "مبتدئ" -> "Easy"
            "intermediate", "medium", "متوسط" -> "Medium"
            "advanced", "hard", "صعب", "متقدم" -> "Hard"
            else -> "Easy"
        }
    }

    private fun sendEffect(effect: GalleryEffect) {
        viewModelScope.launch { _effects.send(effect) }
    }


    companion object {
        const val ALL_ITEMS_CATEGORY = "All Items"
    }
}

