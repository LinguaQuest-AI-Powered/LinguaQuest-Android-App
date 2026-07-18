package com.iti.linguaquest.features.game.presentation.processing.view.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.features.game.presentation.processing.contract.GameWhackState

@Composable
fun GameWhackView(
    state: GameWhackState,
    onLingoWhacked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Coin Counter at the top right
        Text(
            text = "Coins: ${state.currentCoins}",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )

        // Dummy Lingo target (In the future, we will apply state.lingoXPosition & lingoYPosition)
        if (state.isLingoVisible) {
            Button(
                onClick = onLingoWhacked,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text("Whack Lingo!")
            }
        }
    }
}