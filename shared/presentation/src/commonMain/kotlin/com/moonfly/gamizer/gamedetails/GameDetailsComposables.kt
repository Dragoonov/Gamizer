package com.moonfly.gamizer.gamedetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.moonfly.gamizer.base.ErrorMessage
import com.moonfly.gamizer.base.LoadingBar
import gamizer.shared.presentation.generated.resources.Res
import gamizer.shared.presentation.generated.resources.developers
import gamizer.shared.presentation.generated.resources.genres
import gamizer.shared.presentation.generated.resources.info
import gamizer.shared.presentation.generated.resources.platforms
import gamizer.shared.presentation.generated.resources.website
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GameDetailsMainView(viewModel: GameDetailsViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Box {
        when {
            !uiState.isError && !uiState.isLoading ->
                LazyColumn {
                    item {
                        AsyncImage(
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            model = uiState.imageUrl,
                            contentDescription = "Game"
                        )
                        Image(
                            imageVector = if (uiState.isLiked) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                viewModel.handleEvent(
                                    GameDetailsEvent.OnGameLikeChanged(
                                        uiState.id,
                                        uiState.isLiked.not()
                                    )
                                )
                            }
                        )
                        Title(uiState.title)
                        Text(
                            uiState.description,
                            modifier = Modifier.padding(8.dp),
                            maxLines = 10,
                            overflow = TextOverflow.Ellipsis
                        )
                        SecondaryData(uiState)
                    }
                }
            uiState.isError -> ErrorMessage {
                viewModel.handleEvent(GameDetailsEvent.OnRefresh)
            }
            else -> LoadingBar()
        }

    }
}

@Composable
fun Title(text: String) {
    Text(
        text,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 16.dp, start = 16.dp)
    )
}

@Composable
fun SecondaryData(game: GameDetailsState) {
    Section(name = stringResource(Res.string.info)) {
        Data(
            title = stringResource(Res.string.developers),
            value = game.developers.map { it.name }.toString(),
        )
        Data(
            title = stringResource(Res.string.website),
            value = game.website,
        )
        Data(
            title = stringResource(Res.string.platforms),
            value = game.platforms.map { it.name }.toString()
        )
        Data(
            title = stringResource(Res.string.genres),
            value = game.genres.map { it.name }.toString()
        )
    }
}

@Composable
fun Section(name: String, block: @Composable () -> Unit) {
    Text(
        text = name,
        fontSize = 32.sp,
        modifier = Modifier.padding(bottom = 24.dp, start = 8.dp)
    )
    block()
}

@Composable
fun Data(title: String, value: String) {
    Row(
        Modifier
            .height(64.dp)
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        Arrangement.SpaceBetween
    ) {
        Text(text = title)
        Text(text = value)
    }
}