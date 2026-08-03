package com.iti.linguaquest.features.roleplay.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.usecase.GetBossScenariosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

data class RoleplayListState(
    val scenarios: List<BossScenario> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class RoleplayListViewModel @Inject constructor(
    private val getBossScenariosUseCase: GetBossScenariosUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RoleplayListState())
    val state: StateFlow<RoleplayListState> = _state.asStateFlow()

    init {
        loadScenarios()
    }

    private fun loadScenarios() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val lang = Locale.getDefault().language
                val scenarios = getBossScenariosUseCase(lang)
                _state.update { it.copy(scenarios = scenarios, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                Timber.e(e, "Failed to load scenarios")
            }
        }
    }
}

