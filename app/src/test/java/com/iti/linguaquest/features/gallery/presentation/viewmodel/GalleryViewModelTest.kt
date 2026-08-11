package com.iti.linguaquest.features.gallery.presentation.viewmodel

import app.cash.turbine.test
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.state.DataStatus
import com.iti.linguaquest.features.gallery.domain.usecase.DeleteWordUseCase
import com.iti.linguaquest.features.gallery.domain.usecase.GetWordsWithImagesUseCase
import com.iti.linguaquest.features.gallery.domain.usecase.RefreshGalleryUseCase
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryEffect
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryIntent
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import com.iti.linguaquest.features.lockscreen.domain.usecase.GetLockScreenPostedOrOpenedWordsUseCase
import com.iti.linguaquest.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GalleryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getWordsWithImagesUseCase: GetWordsWithImagesUseCase
    private lateinit var refreshGalleryUseCase: RefreshGalleryUseCase
    private lateinit var deleteWordUseCase: DeleteWordUseCase
    private lateinit var getLockScreenPostedOrOpenedWordsUseCase: GetLockScreenPostedOrOpenedWordsUseCase
    private lateinit var observeNetworkStatusUseCase: ObserveNetworkStatusUseCase
    private lateinit var snackbarController: SnackbarController

    private lateinit var viewModel: GalleryViewModel

    private val wordsFlow = MutableSharedFlow<List<WordEntity>>(replay = 1)
    private val lockScreenWordsFlow = MutableSharedFlow<List<LockScreenWord>>(replay = 1)
    private val networkFlow = MutableStateFlow(true)

    @Before
    fun setup() {
        getWordsWithImagesUseCase = mockk()
        refreshGalleryUseCase = mockk()
        deleteWordUseCase = mockk()
        getLockScreenPostedOrOpenedWordsUseCase = mockk()
        observeNetworkStatusUseCase = mockk()
        snackbarController = mockk(relaxed = true)

        every { getWordsWithImagesUseCase() } returns wordsFlow
        every { getLockScreenPostedOrOpenedWordsUseCase() } returns lockScreenWordsFlow
        every { observeNetworkStatusUseCase() } returns networkFlow
        coEvery { refreshGalleryUseCase() } returns LinguaQuestResult.Success(Unit)
    }

    private fun createViewModel() {
        viewModel = GalleryViewModel(
            getWordsWithImagesUseCase,
            refreshGalleryUseCase,
            deleteWordUseCase,
            getLockScreenPostedOrOpenedWordsUseCase,
            observeNetworkStatusUseCase,
            snackbarController
        )
    }

    @Test
    fun init_updatesStateWithWordsAndLockScreenWords_whenUseCasesEmit() = runTest {
        // Given
        val word = WordEntity(1, "Hola", "Hello", "ES", "EN", "Greeting", "")
        val lockScreenWords = List(5) { 
            LockScreenWord(
                id = it, word = "Word$it", translation = "Trans$it", exampleSentence = "ex", difficulty = "Easy", meaning = "meaning$it",
                status = com.iti.linguaquest.core.database.lockscreen.LockScreenWordStatus.PENDING,
                createdAt = 0L, postedAt = 0L, openedAt = 0L,
                nativeLanguage = "ES", targetLanguage = "EN", proficiencyLevel = "Beginner"
            )
        }
        wordsFlow.tryEmit(listOf(word))
        lockScreenWordsFlow.tryEmit(lockScreenWords)
        
        // When
        createViewModel()

        // Then
        viewModel.state.test {
            val initialState = awaitItem()
            assertEquals(listOf(word), initialState.words)
            assertEquals(lockScreenWords, initialState.lockScreenWords)
            assertEquals(listOf("All Items", "Greeting"), initialState.categories)
            assertEquals(listOf("All Items", "Easy", "Medium", "Hard"), initialState.lockScreenCategories)
            assertTrue(initialState.dataStatus is DataStatus.Loaded)
            expectNoEvents()
        }
    }

    @Test
    fun onIntent_RefreshWords_updatesDataStatusToRefreshing_thenLoadedOnSuccess() = runTest {
        // Given
        wordsFlow.tryEmit(emptyList())
        lockScreenWordsFlow.tryEmit(emptyList())
        coEvery { refreshGalleryUseCase() } coAnswers {
            kotlinx.coroutines.delay(10)
            LinguaQuestResult.Success(Unit)
        }
        createViewModel()

        viewModel.state.test {
            awaitItem() // Skip current state
            
            // When
            viewModel.onIntent(GalleryIntent.RefreshWords)
            
            // Then
            val refreshingState = awaitItem()
            assertEquals(DataStatus.Refreshing, refreshingState.dataStatus)
            
            val loadedState = awaitItem()
            assertEquals(DataStatus.Loaded, loadedState.dataStatus)
        }
    }

    @Test
    fun onIntent_RefreshWords_setsErrorStatus_whenNoCacheAndFails() = runTest {
        // Given
        wordsFlow.tryEmit(emptyList())
        lockScreenWordsFlow.tryEmit(emptyList())
        val error = LinguaQuestDataError.Remote.SERVER
        coEvery { refreshGalleryUseCase() } coAnswers {
            kotlinx.coroutines.delay(10)
            LinguaQuestResult.Failure(error)
        }
        createViewModel()

        viewModel.state.test {
            awaitItem() // Skip current state
            
            // When
            viewModel.onIntent(GalleryIntent.RefreshWords)
            
            // Then
            val refreshingState = awaitItem()
            assertEquals(DataStatus.Refreshing, refreshingState.dataStatus)
            
            val errorState = awaitItem()
            assertTrue(errorState.dataStatus is DataStatus.Error)
        }
    }

    @Test
    fun onIntent_RefreshWords_showsOfflineSnackbar_whenCacheExistsAndFailsWithNoInternet() = runTest {
        // Given
        val word = WordEntity(1, "Hola", "Hello", "ES", "EN", "Greeting", "")
        wordsFlow.tryEmit(listOf(word))
        lockScreenWordsFlow.tryEmit(emptyList())
        coEvery { refreshGalleryUseCase() } returns LinguaQuestResult.Failure(LinguaQuestDataError.Remote.NO_INTERNET)
        createViewModel()

        // When
        viewModel.onIntent(GalleryIntent.RefreshWords)
        
        // Then
        coVerify { 
            snackbarController.sendEvent(match { it.type == SnackbarType.INFO }) 
        }
    }

    @Test
    fun onIntent_RefreshWords_showsErrorSnackbar_whenCacheExistsAndFailsWithServerError() = runTest {
        // Given
        val word = WordEntity(1, "Hola", "Hello", "ES", "EN", "Greeting", "")
        wordsFlow.tryEmit(listOf(word))
        lockScreenWordsFlow.tryEmit(emptyList())
        coEvery { refreshGalleryUseCase() } returns LinguaQuestResult.Failure(LinguaQuestDataError.Remote.SERVER)
        createViewModel()

        // When
        viewModel.onIntent(GalleryIntent.RefreshWords)
        
        // Then
        coVerify { 
            snackbarController.sendEvent(match { it.type == SnackbarType.ERROR }) 
        }
    }

    @Test
    fun onIntent_CategorySelected_updatesSelectedCategoryAndFiltersWords() = runTest {
        // Given
        val word1 = WordEntity(1, "Uno", "One", "ES", "EN", "Numbers", "")
        val word2 = WordEntity(2, "Hola", "Hello", "ES", "EN", "Greetings", "")
        wordsFlow.tryEmit(listOf(word1, word2))
        lockScreenWordsFlow.tryEmit(emptyList())
        createViewModel()

        viewModel.state.test {
            awaitItem() // current state
            
            // When
            viewModel.onIntent(GalleryIntent.CategorySelected("Numbers"))
            
            // Then
            val nextState = awaitItem()
            assertEquals("Numbers", nextState.selectedCategory)
            assertEquals(listOf(word1), nextState.filteredWords)
        }
    }

    @Test
    fun onIntent_LockScreenCategorySelected_updatesSelectedLockScreenCategoryAndFiltersWords() = runTest {
        // Given
        val lsWord1 = LockScreenWord(
            id = 1, word = "Uno", translation = "One", exampleSentence = "ex", difficulty = "Easy", meaning = "meaning1",
            status = com.iti.linguaquest.core.database.lockscreen.LockScreenWordStatus.PENDING,
            createdAt = 0L, postedAt = 0L, openedAt = 0L,
            nativeLanguage = "ES", targetLanguage = "EN", proficiencyLevel = "Beginner"
        )
        val lsWord2 = LockScreenWord(
            id = 2, word = "Hola", translation = "Hello", exampleSentence = "ex", difficulty = "Hard", meaning = "meaning2",
            status = com.iti.linguaquest.core.database.lockscreen.LockScreenWordStatus.PENDING,
            createdAt = 0L, postedAt = 0L, openedAt = 0L,
            nativeLanguage = "ES", targetLanguage = "EN", proficiencyLevel = "Advanced"
        )
        wordsFlow.tryEmit(emptyList())
        lockScreenWordsFlow.tryEmit(listOf(lsWord1, lsWord2))
        createViewModel()

        viewModel.state.test {
            awaitItem() // current state
            
            // When
            viewModel.onIntent(GalleryIntent.LockScreenCategorySelected("Hard"))
            
            // Then
            val nextState = awaitItem()
            assertEquals("Hard", nextState.selectedLockScreenCategory)
            assertEquals(listOf(lsWord2), nextState.filteredLockScreenWords)
        }
    }

    @Test
    fun onIntent_WordItemClicked_emitsNavigateToReviewEffect() = runTest {
        // Given
        val word = WordEntity(1, "Uno", "One", "ES", "EN", "Numbers", "")
        wordsFlow.tryEmit(listOf(word))
        lockScreenWordsFlow.tryEmit(emptyList())
        createViewModel()

        viewModel.effects.test {
            // When
            viewModel.onIntent(GalleryIntent.WordItemClicked(1))
            
            // Then
            assertEquals(GalleryEffect.NavigateToReview(word), awaitItem())
        }
    }

    @Test
    fun onIntent_LockScreenWordItemClicked_emitsNavigateToReviewEffect() = runTest {
        // Given
        val lsWord = LockScreenWord(
            id = 1, word = "Uno", translation = "One", exampleSentence = "ex", difficulty = "Easy", meaning = "meaning1",
            status = com.iti.linguaquest.core.database.lockscreen.LockScreenWordStatus.PENDING,
            createdAt = 0L, postedAt = 0L, openedAt = 0L,
            nativeLanguage = "ES", targetLanguage = "EN", proficiencyLevel = "Beginner"
        )
        wordsFlow.tryEmit(emptyList())
        lockScreenWordsFlow.tryEmit(listOf(lsWord))
        createViewModel()

        viewModel.effects.test {
            // When
            viewModel.onIntent(GalleryIntent.LockScreenWordItemClicked(1))
            
            // Then
            val effect = awaitItem() as GalleryEffect.ShowLockScreenWordDialog
            assertEquals(1, effect.wordId)
        }
    }

    @Test
    fun onIntent_DeleteWordClicked_callsDeleteWordUseCase() = runTest {
        // Given
        val word = WordEntity(1, "Uno", "One", "ES", "EN", "Numbers", "")
        wordsFlow.tryEmit(listOf(word))
        lockScreenWordsFlow.tryEmit(emptyList())
        coEvery { deleteWordUseCase(word.id) } returns LinguaQuestResult.Success(Unit)
        createViewModel()

        // When
        viewModel.onIntent(GalleryIntent.DeleteWordClicked(word.id))
        
        // Then
        coVerify(exactly = 1) { deleteWordUseCase(word.id) }
    }
}
