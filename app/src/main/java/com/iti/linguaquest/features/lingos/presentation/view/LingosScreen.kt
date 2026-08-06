package com.iti.linguaquest.features.lingos.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.features.lingos.presentation.view.components.MindReaderCard
import com.iti.linguaquest.features.lingos.presentation.view.components.RoleplayCard
import com.iti.linguaquest.features.lingos.presentation.view.components.VoicePractiseCard

@Composable
fun LingosScreen(
    onNavigateToVoiceGame: () -> Unit,
    onNavigateToRoleplayList: () -> Unit,
    onNavigateToMindReader: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 20.dp)
    ) {
        VoicePractiseCard(
            onStartClick = { _ -> onNavigateToVoiceGame() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        RoleplayCard(
            onStartClick = { _ -> onNavigateToRoleplayList() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        MindReaderCard(
            onStartClick = { _ -> onNavigateToMindReader() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}
