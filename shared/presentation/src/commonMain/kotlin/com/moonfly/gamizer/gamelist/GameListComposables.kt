package com.moonfly.gamizer.gamelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.moonfly.gamizer.base.ErrorMessage
import com.moonfly.gamizer.base.LoadingBar
import com.moonfly.gamizer.base.largeSpace
import com.moonfly.gamizer.base.mediumFont
import com.moonfly.gamizer.base.mediumSpace
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GameListMainView(
    viewModel: GameListViewModel = koinViewModel(),
    onNavigateToDetails: (id: Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                is GameListEffect.NavigateToDetails -> onNavigateToDetails(it.id)
            }
        }
    }

    Box {
        when {
            !uiState.isError && !uiState.isLoading -> {
                LazyColumn {
                    items(uiState.games) {
                        ListItem(it.id, it.title, it.imageUrl) { id ->
                            viewModel.handleEvent(GameListEvent.OnGameClicked(id))
                        }
                    }
                }
            }
            uiState.isError -> ErrorMessage {
                viewModel.handleEvent(GameListEvent.OnRefresh)
            }
            else -> LoadingBar()
        }
    }
}

@Composable
fun ListItem(id: Int, title: String, imageUrl: String, onGameClickListener: (id: Int) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(largeSpace)
            .clickable {
                onGameClickListener(id)
            }
    ) {
        AsyncImage(
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(imageSize).clip(RoundedCornerShape(largeSpace, largeSpace, largeSpace, largeSpace)),
            model = imageUrl,
            contentDescription = "Game",
        )
        Text(text = title, modifier = Modifier.padding(start = mediumSpace), fontSize = mediumFont, fontWeight = FontWeight.Bold)
    }
}

private val imageSize = 100.dp