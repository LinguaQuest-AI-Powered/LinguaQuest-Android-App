package com.iti.linguaquest.features.all_worlds.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsEffect
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsIntent
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsState
import com.iti.linguaquest.features.home.presentation.view.components.WorldDifficulty
import com.iti.linguaquest.features.home.presentation.view.components.WorldItem
import dagger.hilt.android.lifecycle.HiltViewModel
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
class AllWorldsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(AllWorldsState())
    val state: StateFlow<AllWorldsState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AllWorldsEffect>()
    val effect: SharedFlow<AllWorldsEffect> = _effect.asSharedFlow()

    init {
        loadWorlds()
    }

    private fun loadWorlds() {
        _state.update { it.copy(isLoading = true) }
        
        // Mock data
        val worlds = listOf(
            WorldItem(
                id = "kitchen",
                title = "Kitchen World",
                imageRes = R.drawable.kitchen_icon,
                difficulty = WorldDifficulty.EASY,
                progress = 0.40f,
                isCompleted = false
            ),
            WorldItem(
                id = "city",
                title = "City World",
                imageRes = R.drawable.kitchen_icon, // replace with real image if exists
                difficulty = WorldDifficulty.MEDIUM,
                progress = 0.10f,
                isCompleted = false
            ),
            WorldItem(
                id = "park",
                title = "Park World",
                imageRes = R.drawable.kitchen_icon, // replace with real image if exists
                difficulty = WorldDifficulty.EASY,
                progress = 1.0f,
                isCompleted = true
            ),
            WorldItem(
                id = "market",
                title = "Market World",
                imageRes = R.drawable.kitchen_icon, // replace with real image if exists
                difficulty = WorldDifficulty.MEDIUM,
                progress = 0.0f,
                isCompleted = false
            ),
            WorldItem(
                id = "airport",
                title = "Airport World",
                imageRes = R.drawable.kitchen_icon, // replace with real image if exists
                difficulty = WorldDifficulty.HARD,
                progress = 0.0f,
                isCompleted = false,
                unlockLevel = 15
            ),
            WorldItem(
                id = "school",
                title = "School World",
                imageRes = R.drawable.kitchen_icon, // replace with real image if exists
                difficulty = WorldDifficulty.MEDIUM,
                progress = 0.65f,
                isCompleted = false
            )
        )

        _state.update { 
            it.copy(
                isLoading = false,
                worlds = worlds
            ) 
        }
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
