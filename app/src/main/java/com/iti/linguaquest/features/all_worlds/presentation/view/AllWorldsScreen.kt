package com.iti.linguaquest.features.all_worlds.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsEffect
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsIntent
import com.iti.linguaquest.features.all_worlds.presentation.contract.AllWorldsState
import com.iti.linguaquest.features.all_worlds.presentation.view.components.AllWorldsFilterRow
import com.iti.linguaquest.features.all_worlds.presentation.view.components.AllWorldsGrid
import com.iti.linguaquest.features.all_worlds.presentation.view.components.AllWorldsHeader
import com.iti.linguaquest.features.all_worlds.presentation.view.components.AllWorldsTopBar
import com.iti.linguaquest.features.all_worlds.presentation.viewModel.AllWorldsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AllWorldsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToWorldDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AllWorldsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is AllWorldsEffect.NavigateBack -> onNavigateBack()
                is AllWorldsEffect.NavigateToWorldDetails -> onNavigateToWorldDetails(effect.worldId)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F6))
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            AllWorldsContent(
                state = state,
                onIntent = viewModel::onIntent
            )
        }
    }
}

@Composable
fun AllWorldsContent(
    state: AllWorldsState,
    onIntent: (AllWorldsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        AllWorldsTopBar(
            onBackClicked = { onIntent(AllWorldsIntent.OnBackClicked) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        AllWorldsHeader(
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        AllWorldsFilterRow(
            selectedFilter = state.selectedFilter,
            onFilterSelected = { onIntent(AllWorldsIntent.OnFilterSelected(it)) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.worlds_count_format, state.filteredWorlds.size),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = LinguaQuestTheme.colors.iconsColor
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        AllWorldsGrid(
            worlds = state.filteredWorlds,
            onWorldClick = { onIntent(AllWorldsIntent.OnWorldClicked(it)) }
        )
    }
}
