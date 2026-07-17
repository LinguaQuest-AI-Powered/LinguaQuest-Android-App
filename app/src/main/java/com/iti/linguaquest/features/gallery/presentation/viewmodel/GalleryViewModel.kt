package com.iti.linguaquest.features.gallery.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.features.gallery.domain.usecase.DeleteWordUseCase
import com.iti.linguaquest.features.gallery.domain.usecase.GetWordsWithImagesUseCase
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryEffect
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryIntent
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getWordsWithImagesUseCase: GetWordsWithImagesUseCase,
    private val deleteWordUseCase: DeleteWordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GalleryState())
    val state = _state.asStateFlow()

    private val _effects = Channel<GalleryEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        onIntent(GalleryIntent.LoadWords)
    }

    fun onIntent(intent: GalleryIntent) {
        when (intent) {
            GalleryIntent.LoadWords -> loadWords()
            is GalleryIntent.CategorySelected -> filterByCategory(intent.category)
            is GalleryIntent.DeleteWordClicked -> deleteWord(intent.word)
            is GalleryIntent.WordItemClicked -> navigateToWordDetails(intent.wordId)
        }
    }

    private fun loadWords() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorRes = null) }


            getWordsWithImagesUseCase()
                .catch {
                    _state.update { state ->
                        state.copy(isLoading = false, errorRes = R.string.general_error)
                    }
                }.collect { words ->
                    val categories = extractCategories(words)
                    val selectedCategory = _state.value.selectedCategory
                    val filteredWords = filterWords(words, selectedCategory)
                    
                    _state.update {
                        it.copy(
                            isLoading = false,
                            words = words,
                            categories = categories,
                            filteredWords = filteredWords,
                            errorRes = null
                        )
                    }
                }
        }
    }

    private fun filterByCategory(category: String) {
        _state.update {
            it.copy(
                selectedCategory = category,
                filteredWords = filterWords(it.words, category)
            )
        }
    }



    private fun deleteWord(word: WordEntity) {
        viewModelScope.launch {
            deleteWordUseCase(word)
        }
    }

    private fun navigateToWordDetails(wordId: Int) {
        viewModelScope.launch {
            _effects.send(GalleryEffect.NavigateToWordDetails(wordId))
        }
    }

    private fun extractCategories(words: List<WordEntity>): List<String> {
        val uniqueCategories = words.map { it.category }.distinct().filter { it.isNotBlank() }
        return listOf(ALL_ITEMS_CATEGORY) + uniqueCategories
    }

    private fun filterWords(words: List<WordEntity>, category: String): List<WordEntity> {
        return if (category == ALL_ITEMS_CATEGORY) {
            words
        } else {
            words.filter { it.category.equals(category, ignoreCase = true) }
        }
    }

    companion object {
        const val ALL_ITEMS_CATEGORY = "All Items"
    }
}
