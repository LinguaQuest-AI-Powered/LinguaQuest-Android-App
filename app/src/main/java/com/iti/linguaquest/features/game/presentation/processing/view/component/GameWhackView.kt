package com.iti.linguaquest.features.game.presentation.processing.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon
import com.iti.linguaquest.features.game.presentation.processing.contract.GameWhackState

@Composable
fun GameWhackView(
    state: GameWhackState,
    onLingoWhacked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                LingoSpinningIcon(
                    size = 18.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(id = R.string.game_processing_ai_thinking))
            }

            Text(text = stringResource(id = R.string.game_processing_coins_counter, state.currentCoins))
        }

        if (state.isLingoVisible) {
            AppButton3D(
                text = stringResource(id = R.string.game_processing_btn_whack_lingo),
                onClick = onLingoWhacked,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 32.dp)
            )
        }
    }
}