package com.iti.linguaquest.features.mindreader.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.domain.model.GameCost
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.dialog.DialogController
import com.iti.linguaquest.core.sharedComponents.dialog.DialogUiState
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.core.wallet.domain.usecase.AdjustWalletUseCase
import com.iti.linguaquest.core.wallet.domain.usecase.GetWalletUseCase
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderAnswerOption
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderEntity
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameConfig
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameLaunch
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderGameState
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderPopQuizChoice
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderPopQuizQuestion
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderQuestionCandidate
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderResult
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderRewardChallenge
import kotlinx.coroutines.Job
import java.util.UUID
import com.iti.linguaquest.features.mindreader.domain.usecase.BuildMindReaderPopQuizQuestionUseCase
import com.iti.linguaquest.features.mindreader.domain.usecase.GetMindReaderCategoriesUseCase
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderNextTurn
import com.iti.linguaquest.features.mindreader.domain.usecase.GetMindReaderNextTurnUseCase
import com.iti.linguaquest.features.mindreader.domain.usecase.ResolveMindReaderNativeLanguageCodeUseCase
import com.iti.linguaquest.features.mindreader.domain.usecase.ResolveMindReaderRewardUseCase
import com.iti.linguaquest.features.mindreader.domain.usecase.ResolveMindReaderTargetLanguageCodeUseCase
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MindReaderViewModel @Inject constructor(
    private val startMindReaderGameUseCase: StartMindReaderGameUseCase,
    private val getMindReaderNextTurnUseCase: GetMindReaderNextTurnUseCase,
    private val submitMindReaderAnswerUseCase: SubmitMindReaderAnswerUseCase,
    private val resolveMindReaderRewardUseCase: ResolveMindReaderRewardUseCase,
    private val buildMindReaderPopQuizQuestionUseCase: BuildMindReaderPopQuizQuestionUseCase,
    private val verifyMindReaderHonestyUseCase: VerifyMindReaderHonestyUseCase,
    private val getMindReaderCategoriesUseCase: GetMindReaderCategoriesUseCase,
    private val resolveMindReaderNativeLanguageCodeUseCase: ResolveMindReaderNativeLanguageCodeUseCase,
    private val resolveMindReaderTargetLanguageCodeUseCase: ResolveMindReaderTargetLanguageCodeUseCase,
    private val getWalletUseCase: GetWalletUseCase,
    private val adjustWalletUseCase: AdjustWalletUseCase,
    private val snackbarController: SnackbarController,
    private val dialogController: DialogController,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

    private val _state = MutableStateFlow(MindReaderState())
    val state: StateFlow<MindReaderState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MindReaderEffect>()
    val effect: SharedFlow<MindReaderEffect> = _effect.asSharedFlow()

    private var domainState: MindReaderGameState? = null
    private var config: MindReaderGameConfig? = null
    private var turnJob: Job? = null

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
                val nativeLang = resolveMindReaderNativeLanguageCodeUseCase()
                val targetLang = resolveMindReaderTargetLanguageCodeUseCase()
                val categories = getMindReaderCategoriesUseCase()
                _state.update {
                    it.copy(
                        isLoading = false,
                        availableCategories = categories,
                        selectedCategory = categories.firstOrNull(),
                        nativeLanguageCode = nativeLang,
                        targetLanguageCode = targetLang
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
            is MindReaderIntent.TryAgainClicked -> {
                _state.update { it.copy(currentPhase = MindReaderPhase.LOBBY) }
            }
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
                config = launchData.config
                domainState = launchData.state

                _state.update {
                    it.copy(
                        isLoading = false,
                        targetLanguageCode = launchData.languageCode,
                        nativeLanguageCode = launchData.nativeLanguageCode,
                        maxQuestions = launchData.config.maxQuestions
                    )
                }

                processCurrentDomainState()
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun submitAnswer(answer: MindReaderAnswerOption) {
        if (_state.value.currentPhase != MindReaderPhase.PLAYING) return
        val currentState = domainState ?: return
        val questionCandidate = _state.value.currentQuestionCandidate ?: return

        _state.update {
            it.copy(
                currentPhase = MindReaderPhase.THINKING,
                lingoEmotion = LingoEmotion.THINKING
            )
        }

        domainState = submitMindReaderAnswerUseCase(
            state = currentState,
            question = questionCandidate,
            answer = answer
        )

        processCurrentDomainState()
    }

    private fun processCurrentDomainState() {
        val currentConfig = config ?: return
        val currentState = domainState ?: return

        if (currentState.questionCount >= currentConfig.maxQuestions) {
            _state.update {
                it.copy(
                    currentPhase = MindReaderPhase.RESULT,
                    resultInfo = MindReaderResultInfo(
                        isVictory = false,
                        xpEarned = 0,
                        coinsEarned = 0,
                        reason = UiText.StringResource(
                            R.string.mind_reader_reason_max_questions,
                            listOf(currentConfig.maxQuestions)
                        )
                    ),
                    lingoEmotion = LingoEmotion.ANGRY
                )
            }
            return
        }

        if (currentState.questionCount == 0) {
            val selectedCat = _state.value.selectedCategory
            val seedList = selectedCat?.seedQuestions.orEmpty()
            if (seedList.isNotEmpty()) {
                val seed = seedList.random()
                val targetLang = _state.value.targetLanguageCode.lowercase().take(2)
                val nativeLang = _state.value.nativeLanguageCode.lowercase().take(2)
                val targetText = seed[targetLang] ?: seed["en"] ?: seed.values.firstOrNull().orEmpty()
                val nativeText = seed[nativeLang] ?: seed["en"] ?: seed.values.firstOrNull().orEmpty()

                if (targetText.isNotBlank()) {
                    val candidate = MindReaderQuestionCandidate(
                        attributeId = UUID.randomUUID().toString(),
                        targetText = targetText,
                        nativeText = nativeText
                    )
                    _state.update {
                        it.copy(
                            currentPhase = MindReaderPhase.PLAYING,
                            currentQuestionCandidate = candidate,
                            currentQuestion = candidate.targetText,
                            translatedQuestion = candidate.nativeText,
                            showTranslation = false,
                            currentQuestionNumber = 1,
                            lingoEmotion = LingoEmotion.PUZZLED
                        )
                    }
                    return
                }
            }
        }

        turnJob?.cancel()
        turnJob = viewModelScope.launch {
            _state.update { it.copy(currentPhase = MindReaderPhase.THINKING, lingoEmotion = LingoEmotion.THINKING) }

            val nextTurn = getMindReaderNextTurnUseCase(
                category = _state.value.selectedCategory?.id ?: "",
                targetLanguage = _state.value.targetLanguageCode,
                nativeLanguage = _state.value.nativeLanguageCode,
                state = currentState
            )

            when (nextTurn) {
                is MindReaderNextTurn.Question -> {
                    _state.update {
                        it.copy(
                            currentPhase = MindReaderPhase.PLAYING,
                            currentQuestionCandidate = nextTurn.question,
                            currentQuestion = nextTurn.question.targetText,
                            translatedQuestion = nextTurn.question.nativeText,
                            showTranslation = false,
                            currentQuestionNumber = currentState.questionCount + 1,
                            lingoEmotion = LingoEmotion.PUZZLED
                        )
                    }
                }
                is MindReaderNextTurn.Guess -> {
                    _state.update {
                        it.copy(
                            currentPhase = MindReaderPhase.GUESSING_LOADING,
                            lingoEmotion = LingoEmotion.DETECTIVE
                        )
                    }
                    _state.update {
                        it.copy(
                            currentPhase = MindReaderPhase.GUESS_REVEAL,
                            guessResult = nextTurn.guess,
                            lingoEmotion = LingoEmotion.EXCITED
                        )
                    }
                }
                is MindReaderNextTurn.Error -> {
                    _state.update {
                        it.copy(
                            currentPhase = MindReaderPhase.RESULT,
                            resultInfo = MindReaderResultInfo(
                                isVictory = false,
                                xpEarned = 0,
                                coinsEarned = 0,
                                reason = UiText.StringResource(R.string.mind_reader_reason_connection_dropped)
                            ),
                            lingoEmotion = LingoEmotion.ANGRY
                        )
                    }
                }
            }
        }
    }

    private fun handleGuessVerification(isCorrect: Boolean) {
        if (_state.value.currentPhase != MindReaderPhase.GUESS_REVEAL) return
        val guess = _state.value.guessResult ?: return
        val currentConfig = config ?: return

        if (isCorrect) {
            val prebuiltChoices = guess.quizChoices
            if (prebuiltChoices.isNotEmpty()) {
                val categoryId = _state.value.selectedCategory?.id ?: ""
                val choices = prebuiltChoices.map { choice ->
                    if (choice.isCorrect) {
                        MindReaderPopQuizChoice(entity = guess.entity)
                    } else {
                        val dummyEntity = MindReaderEntity(
                            id = UUID.randomUUID().toString(),
                            worldKey = categoryId,
                            targetText = choice.translationText,
                            nativeText = "",
                            emoji = "🤔"
                        )
                        MindReaderPopQuizChoice(entity = dummyEntity)
                    }
                }.shuffled()

                val popQuizQuestion = MindReaderPopQuizQuestion(
                    correctEntity = guess.entity,
                    choices = choices
                )
                _state.update {
                    it.copy(
                        currentPhase = MindReaderPhase.POP_QUIZ,
                        popQuizQuestion = popQuizQuestion
                    )
                }
                return
            }

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
                    val history = domainState?.history ?: return@launch
                    val result = MindReaderResult.Victory(
                        guess = guess,
                        history = history,
                        rewardCoins = currentConfig.correctRewardCoins,
                        rewardXp = currentConfig.correctRewardXp
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
        if (_state.value.currentPhase != MindReaderPhase.POP_QUIZ) return
        val currentConfig = config ?: return
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
                claimedWord = challenge.correctEntity.targetText
            )

            val result = resolveMindReaderRewardUseCase(
                challenge = challenge,
                contradictionResult = contradiction,
                config = currentConfig,
                history = currentState.history
            )

            handleResolution(result)
        }
    }

    private fun handleStumpSubmit() {
        if (_state.value.currentPhase != MindReaderPhase.STUMP) return
        val currentConfig = config ?: return
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
                config = currentConfig,
                history = currentState.history
            )

            handleResolution(result)
        }
    }

    private fun handleResolution(result: MindReaderResult) {
        when (result) {
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
            else -> Unit
        }
    }

    private fun toggleTranslation() {
        if (_state.value.showTranslation) {
            _state.update { it.copy(showTranslation = false) }
            return
        }

        val cost = GameCost.MIND_READER_TRANSLATION.coins
        if (_state.value.coinBalance < cost) {
            viewModelScope.launch {
                snackbarController.sendEvent(
                    SnackbarEvent(
                        message = UiText.StringResource(R.string.mind_reader_error_not_enough_coins),
                        type = SnackbarType.WARNING
                    )
                )
            }
            return
        }

        dialogController.show(
            DialogUiState(
                title = UiText.StringResource(R.string.mind_reader_translate_confirm_title),
                message = UiText.StringResource(
                    R.string.mind_reader_translate_confirm_message,
                    listOf(cost)
                ),
                confirmText = UiText.StringResource(R.string.confirm),
                dismissText = UiText.StringResource(R.string.cancel),
                onConfirm = {
                    dialogController.hide()
                    viewModelScope.launch {
                        when (val result = adjustWalletUseCase(xpDelta = 0, coinsDelta = -cost)) {
                            is LinguaQuestResult.Success -> {
                                _state.update { it.copy(showTranslation = true) }
                            }
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
                },
                onDismiss = {
                    dialogController.hide()
                }
            )
        )
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
        val text = if (isGuess) _state.value.guessResult?.entity?.targetText else _state.value.currentQuestion
        val lang = _state.value.targetLanguageCode
        if (!text.isNullOrEmpty()) {
            viewModelScope.launch { _effect.emit(MindReaderEffect.PlayAudio(text, lang)) }
        }
    }
}
