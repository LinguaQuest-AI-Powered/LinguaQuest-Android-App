package com.iti.linguaquest.features.mindreader.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.navigation.RootScreen
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderDataset
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameLaunch
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameState
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderPopQuizChoice
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderRewardChallenge
import com.iti.linguaquest.features.mindreader.domain.usecase.GetMindReaderCategoriesUseCase
import com.iti.linguaquest.features.mindreader.domain.usecase.BuildMindReaderPopQuizQuestionUseCase
import com.iti.linguaquest.features.mindreader.domain.usecase.CheckMindReaderContradictionsUseCase
import com.iti.linguaquest.features.mindreader.domain.usecase.GetMindReaderCurrentResultUseCase
import com.iti.linguaquest.features.mindreader.domain.usecase.GetMindReaderNextQuestionUseCase
import com.iti.linguaquest.features.mindreader.domain.usecase.ResolveMindReaderRewardUseCase
import com.iti.linguaquest.features.mindreader.domain.usecase.StartMindReaderGameUseCase
import com.iti.linguaquest.features.mindreader.domain.usecase.SubmitMindReaderAnswerUseCase
import com.iti.linguaquest.features.mindreader.presentation.contract.LingoEmotion
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderEffect
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderIntent
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderPhase
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderResultInfo
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MindReaderViewModel @Inject constructor(
    private val startMindReaderGameUseCase: StartMindReaderGameUseCase,
    private val getMindReaderNextQuestionUseCase: GetMindReaderNextQuestionUseCase,
    private val submitMindReaderAnswerUseCase: SubmitMindReaderAnswerUseCase,
    private val getMindReaderCurrentResultUseCase: GetMindReaderCurrentResultUseCase,
    private val resolveMindReaderRewardUseCase: ResolveMindReaderRewardUseCase,
    private val checkMindReaderContradictionsUseCase: CheckMindReaderContradictionsUseCase,
    private val buildMindReaderPopQuizQuestionUseCase: BuildMindReaderPopQuizQuestionUseCase,
    private val getMindReaderCategoriesUseCase: GetMindReaderCategoriesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(MindReaderState())
    val state: StateFlow<MindReaderState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MindReaderEffect>()
    val effect: SharedFlow<MindReaderEffect> = _effect.asSharedFlow()

    private var domainState: MindReaderGameState? = null
    private var dataset: MindReaderDataset? = null

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val categories = getMindReaderCategoriesUseCase()
                _state.update { 
                    it.copy(
                        isLoading = false,
                        availableCategories = categories,
                        selectedCategory = categories.firstOrNull()
                    ) 
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onIntent(intent: MindReaderIntent) {
        when (intent) {
            is MindReaderIntent.CategorySelected -> {
                _state.update { it.copy(selectedCategory = intent.category) }
            }
            is MindReaderIntent.StartGameClicked -> startGame()
            is MindReaderIntent.AnswerClicked -> submitAnswer(intent.answer)
            is MindReaderIntent.TranslateClicked -> toggleTranslation()
            is MindReaderIntent.GuessVerifiedCorrect -> handleGuessVerification(true)
            is MindReaderIntent.GuessVerifiedIncorrect -> handleGuessVerification(false)
            is MindReaderIntent.PopQuizAnswered -> handlePopQuizAnswer(intent.choice)
            is MindReaderIntent.StumpWordSelected -> handleStumpSelection(intent.entity)
            is MindReaderIntent.PlayAudioClicked -> playAudio(isGuess = false)
            is MindReaderIntent.PlayGuessAudioClicked -> playAudio(isGuess = true)
            is MindReaderIntent.TryAgainClicked -> startGame()
            is MindReaderIntent.ReturnToHomeClicked -> {
                viewModelScope.launch { _effect.emit(MindReaderEffect.NavigateBack) }
            }
        }
    }

    private fun startGame() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, currentPhase = MindReaderPhase.LOBBY) }
            try {
                val launchData: MindReaderGameLaunch = startMindReaderGameUseCase(
                    worldKey = _state.value.selectedCategory?.id
                )
                dataset = launchData.dataset
                domainState = launchData.state
                
                _state.update {
                    it.copy(
                        isLoading = false,
                        targetLanguageCode = launchData.languageCode,
                        maxQuestions = launchData.dataset.config.maxQuestions,
                        coinBalance = 0,
                        xpBalance = 0
                    )
                }
                
                processCurrentDomainState()
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun submitAnswer(answer: MindReaderAnswerOption) {
        val currentDs = dataset ?: return
        val currentState = domainState ?: return
        val attributeId = getMindReaderNextQuestionUseCase(currentDs, currentState)?.attributeId ?: return

        domainState = submitMindReaderAnswerUseCase(
            dataset = currentDs,
            state = currentState,
            attributeId = attributeId,
            answer = answer
        )

        processCurrentDomainState()
    }

    private fun processCurrentDomainState() {
        val currentDs = dataset ?: return
        val currentState = domainState ?: return

        val result = getMindReaderCurrentResultUseCase(currentDs, currentState)
        
        when (result) {
            is MindReaderResult.Playing -> {
                val nextQuestion = getMindReaderNextQuestionUseCase(currentDs, currentState)
                if (nextQuestion != null) {
                    _state.update {
                        it.copy(
                            currentPhase = MindReaderPhase.PLAYING,
                            currentQuestion = nextQuestion.question.resolve(it.targetLanguageCode),
                            translatedQuestion = nextQuestion.question.resolve("en"),
                            showTranslation = false,
                            currentQuestionNumber = currentState.questionCount + 1,
                            lingoEmotion = determineLingoEmotion(currentState.candidates.size)
                        )
                    }
                }
            }
            is MindReaderResult.Guessing -> {
                viewModelScope.launch {
                    _state.update { it.copy(currentPhase = MindReaderPhase.GUESSING_LOADING, lingoEmotion = LingoEmotion.DETECTIVE) }
                    delay(2500)
                    _state.update {
                        it.copy(
                            currentPhase = MindReaderPhase.GUESS_REVEAL,
                            guessResult = result.guess,
                            lingoEmotion = LingoEmotion.EXCITED
                        )
                    }
                }
            }
            is MindReaderResult.PopQuiz -> {
                _state.update {
                    it.copy(
                        currentPhase = MindReaderPhase.POP_QUIZ,
                        popQuizQuestion = result.question,
                        guessResult = result.originalGuess
                    )
                }
            }
            is MindReaderResult.Stump -> {
                _state.update {
                    it.copy(
                        currentPhase = MindReaderPhase.STUMP,
                        stumpCandidates = result.candidates,
                        guessResult = result.originalGuess
                    )
                }
            }
            is MindReaderResult.Victory -> {
                _state.update {
                    it.copy(
                        currentPhase = MindReaderPhase.RESULT,
                        resultInfo = MindReaderResultInfo(
                            isVictory = true,
                            xpEarned = result.rewardXp,
                            coinsEarned = result.rewardCoins
                        ),
                        lingoEmotion = LingoEmotion.CELEBRATING
                    )
                }
            }
            is MindReaderResult.Busted -> {
                _state.update {
                    it.copy(
                        currentPhase = MindReaderPhase.RESULT,
                        resultInfo = MindReaderResultInfo(
                            isVictory = false,
                            xpEarned = 0,
                            coinsEarned = 0,
                            reason = result.reason
                        ),
                        lingoEmotion = LingoEmotion.ANGRY
                    )
                }
            }
            is MindReaderResult.Timeout -> {
                _state.update {
                    it.copy(
                        currentPhase = MindReaderPhase.RESULT,
                        resultInfo = MindReaderResultInfo(
                            isVictory = false,
                            xpEarned = 0,
                            coinsEarned = 0,
                            reason = "Too many questions!"
                        ),
                        lingoEmotion = LingoEmotion.ANGRY
                    )
                }
            }
        }
    }

    private fun handleGuessVerification(isCorrect: Boolean) {
        val currentDs = dataset ?: return
        val currentState = domainState ?: return
        val guess = _state.value.guessResult ?: return
        
        if (isCorrect) {
            val popQuizQuestion = buildMindReaderPopQuizQuestionUseCase(
                correctEntity = guess.entity,
                candidatePool = currentDs.entities
            )
            _state.update {
                it.copy(
                    currentPhase = MindReaderPhase.POP_QUIZ,
                    popQuizQuestion = popQuizQuestion
                )
            }
        } else {
            _state.update {
                it.copy(
                    currentPhase = MindReaderPhase.STUMP,
                    stumpCandidates = currentDs.entities
                )
            }
        }
    }

    private fun handlePopQuizAnswer(choice: MindReaderPopQuizChoice) {
        val currentDs = dataset ?: return
        val currentState = domainState ?: return
        val challenge = MindReaderRewardChallenge.PopQuiz(
            correctEntity = _state.value.guessResult!!.entity,
            selectedEntity = choice.entity
        )
        
        val contradiction = checkMindReaderContradictionsUseCase(
            history = currentState.history,
            evaluatedEntity = challenge.correctEntity,
            attributes = currentDs.attributes
        )
        
        val result = resolveMindReaderRewardUseCase(
            challenge = challenge,
            contradictionResult = contradiction,
            config = currentDs.config,
            history = currentState.history
        )
        
        handleResolution(result)
    }

    private fun handleStumpSelection(entity: MindReaderEntity) {
        val currentDs = dataset ?: return
        val currentState = domainState ?: return
        val challenge = MindReaderRewardChallenge.Stump(
            selectedEntity = entity
        )
        
        val contradiction = checkMindReaderContradictionsUseCase(
            history = currentState.history,
            evaluatedEntity = challenge.selectedEntity,
            attributes = currentDs.attributes
        )
        
        val result = resolveMindReaderRewardUseCase(
            challenge = challenge,
            contradictionResult = contradiction,
            config = currentDs.config,
            history = currentState.history
        )
        
        handleResolution(result)
    }
    
    private fun handleResolution(result: MindReaderResult) {
        when(result) {
            is MindReaderResult.Victory -> {
                 _state.update {
                    it.copy(
                        currentPhase = MindReaderPhase.RESULT,
                        resultInfo = MindReaderResultInfo(true, result.rewardXp, result.rewardCoins),
                        lingoEmotion = LingoEmotion.CELEBRATING
                    )
                }
            }
            is MindReaderResult.Busted -> {
                 _state.update {
                    it.copy(
                        currentPhase = MindReaderPhase.RESULT,
                        resultInfo = MindReaderResultInfo(false, 0, 0, result.reason),
                        lingoEmotion = LingoEmotion.ANGRY
                    )
                }
            }
            else -> {}
        }
    }

    private fun toggleTranslation() {
        val cost = dataset?.config?.translationCost ?: 0
        if (_state.value.coinBalance >= cost) {
            _state.update {
                it.copy(
                    showTranslation = !it.showTranslation,
                    coinBalance = it.coinBalance - (if (it.showTranslation) 0 else cost)
                )
            }
        }
    }

    private fun playAudio(isGuess: Boolean) {
        val text = if (isGuess) _state.value.guessResult?.entity?.resolveTranslation(_state.value.targetLanguageCode) else _state.value.currentQuestion
        val lang = _state.value.targetLanguageCode
        if (!text.isNullOrEmpty()) {
            viewModelScope.launch { _effect.emit(MindReaderEffect.PlayAudio(text, lang)) }
        }
    }
    
    private fun determineLingoEmotion(remainingCandidates: Int): LingoEmotion {
        return when {
            remainingCandidates > 15 -> LingoEmotion.PUZZLED
            remainingCandidates > 5 -> LingoEmotion.THINKING
            else -> LingoEmotion.EXCITED
        }
    }
}
