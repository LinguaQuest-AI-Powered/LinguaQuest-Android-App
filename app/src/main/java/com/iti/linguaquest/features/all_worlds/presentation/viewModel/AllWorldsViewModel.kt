package com.iti.linguaquest.features.all_worlds.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsEffect
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsIntent
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsState
import com.iti.linguaquest.features.home.presentation.view.components.WorldDifficulty
import com.iti.linguaquest.features.home.presentation.view.components.WorldItem
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.all_worlds.domain.usecase.GetWorldsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.features.all_worlds.domain.model.World
import com.iti.linguaquest.features.all_worlds.domain.model.WorldDifficulty as DomainWorldDifficulty
import com.iti.linguaquest.R
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
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
class AllWorldsViewModel @Inject constructor(
    private val getWorldsUseCase: GetWorldsUseCase,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )


    private val _state = MutableStateFlow(AllWorldsState())
    val state: StateFlow<AllWorldsState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AllWorldsEffect>()
    val effect: SharedFlow<AllWorldsEffect> = _effect.asSharedFlow()

    init {
        loadWorlds()
    }

    private fun loadWorlds() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, hasError = false, errorMessage = null) }

            when (val result = getWorldsUseCase()) {
                is LinguaQuestResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            hasError = false,
                            worlds = result.data.worlds.map { world -> world.toWorldItem() }
                        )
                    }
                }

                is LinguaQuestResult.Failure -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            hasError = true,
                            errorMessage = (result.error as? LinguaQuestDataError)?.toUiText()
                                ?: UiText.StringResource(R.string.error_generic)
                        )
                    }
                }
            }
        }
    }

    private fun World.toWorldItem(): WorldItem {
        val safeUrl = imageUrl.orEmpty()
        return WorldItem(
            id = id,
            title = UiText.DynamicString(name.ifEmpty { "World $id" }),
            imageSource = if (safeUrl.startsWith("http")) safeUrl else R.drawable.kitchen_icon,
            difficulty = when (difficulty) {
                DomainWorldDifficulty.EASY -> WorldDifficulty.EASY
                DomainWorldDifficulty.MEDIUM -> WorldDifficulty.MEDIUM
                DomainWorldDifficulty.HARD -> WorldDifficulty.HARD
            },
            progress = (progressPercent ?: 0) / 100f,
            isCompleted = (completedLevels > 0 && completedLevels >= totalLevels) || (progressPercent ?: 0) >= 100,
            unlockLevel = null
        )
    }

    fun onIntent(intent: AllWorldsIntent) {
        when (intent) {
            is AllWorldsIntent.OnFilterSelected -> {
                _state.update { it.copy(selectedFilter = intent.filter) }
            }

            is AllWorldsIntent.OnWorldClicked -> {
                emitEffect(AllWorldsEffect.NavigateToWorldDetails(intent.world.id))
            }

            AllWorldsIntent.OnBackClicked -> {
                emitEffect(AllWorldsEffect.NavigateBack)
            }
            AllWorldsIntent.OnRetry -> {
                loadWorlds()
            }
        }
    }

    private fun emitEffect(effect: AllWorldsEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
