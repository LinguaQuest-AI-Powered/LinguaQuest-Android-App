package com.iti.linguaquest.features.gallery.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.sharedComponents.offline.NoInternetMiniPopup
import com.iti.linguaquest.core.sharedComponents.state.StatefulContentContainer
import com.iti.linguaquest.core.navigation.SharedBackgroundState
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryEffect
import com.iti.linguaquest.features.gallery.presentation.contract.GalleryIntent
import com.iti.linguaquest.features.gallery.presentation.viewmodel.GalleryViewModel
import com.iti.linguaquest.features.home.utils.calculatePopupOffset
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GalleryScreen(
    onNavigateToReview: (WordEntity) -> Unit,
    onNavigateHome: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    var showOfflinePopup by remember { mutableStateOf(false) }
    var offlinePopupAnchor by remember { mutableStateOf<Rect?>(null) }
    var offlinePopupSize by remember { mutableStateOf(IntSize.Zero) }

    fun guardOnline(anchor: Rect? = null, action: () -> Unit) {
        if (isOnline) {
            action()
        } else {
            offlinePopupAnchor = anchor
            showOfflinePopup = true
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is GalleryEffect.NavigateToReview -> onNavigateToReview(effect.word)
            }
        }
    }

    val isEmpty = !state.hasData

    LaunchedEffect(isEmpty) {
        SharedBackgroundState.showBackground = true
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        StatefulContentContainer(
            dataStatus = state.dataStatus,
            onRetry = { viewModel.onIntent(GalleryIntent.LoadWords) },
            onRefresh = { viewModel.onIntent(GalleryIntent.RefreshWords) },
            onErrorDismiss = onNavigateHome,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.lingo_gellary_icon),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column {
                        Text(
                            text = stringResource(R.string.my_captures),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isEmpty) stringResource(R.string.captures_so_far) else stringResource(R.string.objects_collected, state.words.size),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                GalleryContent(
                    state = state,
                    onIntent = viewModel::onIntent,
                    onWordClick = { wordId: Int, anchor: Rect ->
                        guardOnline(anchor) {
                            viewModel.onIntent(GalleryIntent.WordItemClicked(wordId))
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        if (showOfflinePopup) {
            val popupOffset = remember(
                offlinePopupAnchor,
                offlinePopupSize,
                configuration,
                density
            ) {
                calculatePopupOffset(
                    anchor = offlinePopupAnchor,
                    popupSize = offlinePopupSize,
                    screenWidthDp = configuration.screenWidthDp,
                    screenHeightDp = configuration.screenHeightDp,
                    density = density
                )
            }

            NoInternetMiniPopup(
                isOnline = isOnline,
                modifier = Modifier
                    .offset { popupOffset }
                    .onSizeChanged { offlinePopupSize = it },
                onDismiss = {
                    showOfflinePopup = false
                    offlinePopupAnchor = null
                }
            )
        }
    }
}
