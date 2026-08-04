package com.iti.linguaquest.features.mindreader.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.core.wallet.domain.usecase.AdjustWalletUseCase
import com.iti.linguaquest.core.wallet.domain.usecase.GetWalletUseCase
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
import com.iti.linguaquest.features.mindreader.domain.usecase.GetMindReaderNextTurnUseCase
import com.iti.linguaquest.features.mindreader.domain.usecase.ResolveMindReaderRewardUseCase
import com.iti.linguaquest.features.mindreader.domain.usecase.StartMindReaderGameUseCase
import com.iti.linguaquest.features.mindreader.domain.usecase.SubmitMindReaderAnswerUseCase
import com.iti.linguaquest.features.mindreader.domain.usecase.VerifyMindReaderHonestyUseCase
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
    private val getMindReaderNextTurnUseCase: com.iti.linguaquest.features.mindreader.domain.usecase.GetMindReaderNextTurnUseCase,
    private val submitMindReaderAnswerUseCase: SubmitMindReaderAnswerUseCase,
    private val resolveMindReaderRewardUseCase: ResolveMindReaderRewardUseCase,
    private val buildMindReaderPopQuizQuestionUseCase: BuildMindReaderPopQuizQuestionUseCase,
    private val verifyMindReaderHonestyUseCase: VerifyMindReaderHonestyUseCase,
    private val getMindReaderCategoriesUseCase: GetMindReaderCategoriesUseCase,
    private val getWalletUseCase: GetWalletUseCase,
    private val adjustWalletUseCase: AdjustWalletUseCase,
    private val snackbarController: SnackbarController,
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
            getWalletUseCase().collect { wallet ->
                _state.update {
                    it.copy(
                        coinBalance = wallet.coins,
                        xpBalance = wallet.xp
                    )
                }
            }
        }

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
            is MindReaderIntent.StumpInputValueChanged -> {
                _state.update { it.copy(stumpInputValue = intent.value) }
            }
            is MindReaderIntent.StumpSubmitClicked -> handleStumpSubmit()
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
                        nativeLanguageCode = launchData.nativeLanguageCode,
                        maxQuestions = launchData.dataset.config.maxQuestions
                    )
                }
                
                processCurrentDomainState()
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun submitAnswer(answer: MindReaderAnswerOption) {
        val currentState = domainState ?: return
        val questionCandidate = _state.value.currentQuestionCandidate ?: return

        domainState = submitMindReaderAnswerUseCase(
            state = currentState,
            question = questionCandidate,
            answer = answer
        )

        processCurrentDomainState()
    }

    private fun processCurrentDomainState() {
        val currentDs = dataset ?: return
        val currentState = domainState ?: return

        if (currentState.questionCount >= currentDs.config.maxQuestions) {
            _state.update {
                it.copy(
                    currentPhase = MindReaderPhase.RESULT,
                    resultInfo = MindReaderResultInfo(
                        isVictory = false,
                        xpEarned = 0,
                        coinsEarned = 0,
                        reason = "I couldn't guess it in ${currentDs.config.maxQuestions} questions!"
                    ),
                    lingoEmotion = LingoEmotion.ANGRY
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(currentPhase = MindReaderPhase.THINKING, lingoEmotion = LingoEmotion.THINKING) }
            
            val nextTurn = getMindReaderNextTurnUseCase(
                category = _state.value.selectedCategory?.id ?: "",
                targetLanguage = _state.value.targetLanguageCode,
                nativeLanguage = _state.value.nativeLanguageCode,
                state = currentState
            )

            when (nextTurn) {
                is com.iti.linguaquest.features.mindreader.domain.usecase.MindReaderNextTurn.Question -> {
                    _state.update {
                        it.copy(
                            currentPhase = MindReaderPhase.PLAYING,
                            currentQuestionCandidate = nextTurn.question,
                            currentQuestion = nextTurn.question.question.resolve(it.targetLanguageCode),
                            translatedQuestion = nextTurn.question.question.resolve(it.nativeLanguageCode),
                            showTranslation = false,
                            currentQuestionNumber = currentState.questionCount + 1,
                            lingoEmotion = LingoEmotion.PUZZLED
                        )
                    }
                }
                is com.iti.linguaquest.features.mindreader.domain.usecase.MindReaderNextTurn.Guess -> {
                    _state.update { 
                        it.copy(
                            currentPhase = MindReaderPhase.GUESSING_LOADING, 
                            lingoEmotion = LingoEmotion.DETECTIVE
                        ) 
                    }
                    delay(2500)
                    _state.update {
                        it.copy(
                            currentPhase = MindReaderPhase.GUESS_REVEAL,
                            guessResult = nextTurn.guess,
                            lingoEmotion = LingoEmotion.EXCITED
                        )
                    }
                }
                is com.iti.linguaquest.features.mindreader.domain.usecase.MindReaderNextTurn.Error -> {
                    _state.update {
                        it.copy(
                            currentPhase = MindReaderPhase.RESULT,
                            resultInfo = MindReaderResultInfo(
                                isVictory = false,
                                xpEarned = 0,
                                coinsEarned = 0,
                                reason = "My connection dropped! Let's try again."
                            ),
                            lingoEmotion = LingoEmotion.ANGRY
                        )
                    }
                }
            }
        }
    }

    private fun handleGuessVerification(isCorrect: Boolean) {
        val guess = _state.value.guessResult ?: return
        val currentDs = dataset ?: return
        
        if (isCorrect) {
            viewModelScope.launch {
                _state.update { it.copy(currentPhase = MindReaderPhase.GUESSING_LOADING, lingoEmotion = LingoEmotion.DETECTIVE) }
                val popQuizQuestion = buildMindReaderPopQuizQuestionUseCase(
                    categoryContext = _state.value.selectedCategory?.id ?: "",
                    targetLanguage = _state.value.targetLanguageCode,
                    nativeLanguage = _state.value.nativeLanguageCode,
                    correctEntity = guess.entity
                )
                if (popQuizQuestion != null) {
                    _state.update {
                        it.copy(
                            currentPhase = MindReaderPhase.POP_QUIZ,
                            popQuizQuestion = popQuizQuestion
                        )
                    }
                } else {
                    // Fallback to victory if pop quiz generation fails
                    val history = domainState?.history ?: return@launch
                    val result = MindReaderResult.Victory(
                        guess = guess,
                        history = history,
                        rewardCoins = currentDs.config.correctRewardCoins,
                        rewardXp = currentDs.config.correctRewardXp
                    )
                    handleResolution(result)
                }
            }
        } else {
            _state.update {
                it.copy(
                    currentPhase = MindReaderPhase.STUMP,
                    stumpInputValue = ""
                )
            }
        }
    }

    private fun handlePopQuizAnswer(choice: MindReaderPopQuizChoice) {
        val currentDs = dataset ?: return
        val currentState = domainState ?: return
        val categoryId = _state.value.selectedCategory?.id ?: ""
        val targetLang = _state.value.targetLanguageCode

        viewModelScope.launch {
            _state.update { it.copy(currentPhase = MindReaderPhase.THINKING, lingoEmotion = LingoEmotion.DETECTIVE) }

            val challenge = MindReaderRewardChallenge.PopQuiz(
                correctEntity = _state.value.guessResult!!.entity,
                selectedEntity = choice.entity
            )
            
            val contradiction = verifyMindReaderHonestyUseCase(
                categoryContext = categoryId,
                targetLanguage = targetLang,
                feedbackLanguage = _state.value.nativeLanguageCode,
                history = currentState.history,
                claimedWord = challenge.correctEntity.resolveTranslation(targetLang)
            )
            
            val result = resolveMindReaderRewardUseCase(
                challenge = challenge,
                contradictionResult = contradiction,
                config = currentDs.config,
                history = currentState.history
            )
            
            handleResolution(result)
        }
    }

    private fun handleStumpSubmit() {
        val currentDs = dataset ?: return
        val currentState = domainState ?: return
        val categoryId = _state.value.selectedCategory?.id ?: ""
        val targetLang = _state.value.targetLanguageCode
        val nativeLang = _state.value.nativeLanguageCode
        val inputValue = _state.value.stumpInputValue

        if (inputValue.isBlank()) return

        viewModelScope.launch {
            _state.update { it.copy(currentPhase = MindReaderPhase.THINKING, lingoEmotion = LingoEmotion.DETECTIVE) }

            val contradiction = verifyMindReaderHonestyUseCase(
                categoryContext = categoryId,
                targetLanguage = targetLang,
                feedbackLanguage = nativeLang,
                history = currentState.history,
                claimedWord = inputValue
            )

            val challenge = MindReaderRewardChallenge.Stump(
                selectedEntity = contradiction.evaluatedEntity
            )
            
            val result = resolveMindReaderRewardUseCase(
                challenge = challenge,
                contradictionResult = contradiction,
                config = currentDs.config,
                history = currentState.history
            )
            
            handleResolution(result)
        }
    }
    
    private fun handleResolution(result: MindReaderResult) {
        when(result) {
            is MindReaderResult.Victory -> {
                awardRewards(xp = result.rewardXp, coins = result.rewardCoins)
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
        if (!_state.value.showTranslation) {
            if (_state.value.coinBalance >= cost) {
                if (cost > 0) {
                    viewModelScope.launch {
                        when (val result = adjustWalletUseCase(xpDelta = 0, coinsDelta = -cost)) {
                            is LinguaQuestResult.Success -> Unit
                            is LinguaQuestResult.Failure -> {
                                snackbarController.sendEvent(
                                    SnackbarEvent(
                                        message = result.error.toUiText(),
                                        type = SnackbarType.ERROR
                                    )
                                )
                            }
                        }
                    }
                }
                _state.update { it.copy(showTranslation = true) }
            } else {
                viewModelScope.launch {
                    snackbarController.sendEvent(
                        SnackbarEvent(
                            message = UiText.DynamicString("Not enough coins for translation"),
                            type = SnackbarType.WARNING
                        )
                    )
                }
            }
        } else {
            _state.update { it.copy(showTranslation = false) }
        }
    }

    private fun awardRewards(xp: Int, coins: Int) {
        if (xp > 0 || coins > 0) {
            viewModelScope.launch {
                when (val result = adjustWalletUseCase(xpDelta = xp, coinsDelta = coins)) {
                    is LinguaQuestResult.Success -> Unit
                    is LinguaQuestResult.Failure -> {
                        snackbarController.sendEvent(
                            SnackbarEvent(
                                message = result.error.toUiText(),
                                type = SnackbarType.ERROR
                            )
                        )
                    }
                }
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
