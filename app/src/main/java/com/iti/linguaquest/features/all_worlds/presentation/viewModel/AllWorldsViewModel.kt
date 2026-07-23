package com.iti.linguaquest.features.all_worlds.presentation.viewModel

import android.util.Log
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
import com.iti.linguaquest.features.all_worlds.domain.model.World
import com.iti.linguaquest.features.all_worlds.domain.model.WorldStatus
import com.iti.linguaquest.features.all_worlds.domain.model.WorldDifficulty as DomainWorldDifficulty
import com.iti.linguaquest.R
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
class AllWorldsViewModel @Inject constructor(private val getWorldsUseCase : GetWorldsUseCase) : ViewModel() {

    private val _state = MutableStateFlow(AllWorldsState())
    val state: StateFlow<AllWorldsState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AllWorldsEffect>()
    val effect: SharedFlow<AllWorldsEffect> = _effect.asSharedFlow()

    init {
        loadWorlds()
    }

    private fun loadWorlds() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = getWorldsUseCase(null)) {
                is LinguaQuestResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            worlds = result.data.worlds.map { world -> world.toWorldItem() }
                        )
                    }
                }
                is LinguaQuestResult.Failure -> {
                    Log.e("AllWorldsViewModel", "Error loading worlds: ${result.error}")
                    _state.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }

    private fun World.toWorldItem(): WorldItem {
        return WorldItem(
            id = id,
            title = UiText.DynamicString(name),
            imageSource = if (imageUrl.isNotBlank() && imageUrl.startsWith("http")) imageUrl else R.drawable.kitchen_icon,
            difficulty = when (difficulty) {
                DomainWorldDifficulty.EASY -> WorldDifficulty.EASY
                DomainWorldDifficulty.MEDIUM -> WorldDifficulty.MEDIUM
                DomainWorldDifficulty.HARD -> WorldDifficulty.HARD
            },
            progress = progressPercent / 100f,
            isCompleted = status == WorldStatus.COMPLETED,
            unlockLevel = if (status == WorldStatus.LOCKED) 1 else null
        )
    }

    fun onIntent(intent: AllWorldsIntent) {
        when (intent) {
            is AllWorldsIntent.OnFilterSelected -> {
                _state.update { it.copy(selectedFilter = intent.filter) }
            }
            is AllWorldsIntent.OnWorldClicked -> {
                if (intent.world.unlockLevel == null) {
                    emitEffect(AllWorldsEffect.NavigateToWorldDetails(intent.world.id))
                }
            }
            AllWorldsIntent.OnBackClicked -> {
                emitEffect(AllWorldsEffect.NavigateBack)
            }
        }
    }

    private fun emitEffect(effect: AllWorldsEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
