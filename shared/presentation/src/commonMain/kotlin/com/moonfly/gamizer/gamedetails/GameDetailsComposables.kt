package com.moonfly.gamizer.gamedetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import coil3.compose.AsyncImage
import com.moonfly.gamizer.base.ErrorMessage
import com.moonfly.gamizer.base.LoadingBar
import com.moonfly.gamizer.base.extraLargeFont
import com.moonfly.gamizer.base.extraLargeSpace
import com.moonfly.gamizer.base.largeFont
import com.moonfly.gamizer.base.largeSpace
import com.moonfly.gamizer.base.mediumSpace
import gamizer.shared.presentation.generated.resources.Res
import gamizer.shared.presentation.generated.resources.developers
import gamizer.shared.presentation.generated.resources.genres
import gamizer.shared.presentation.generated.resources.no_data
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
                Column {
                    AsyncImage(
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxWidth().padding(bottom = largeSpace),
                        model = uiState.imageUrl,
                        contentDescription = "Game"
                    )
                    Title(uiState.title)
                    Text(
                        uiState.description,
                        modifier = Modifier.padding(mediumSpace),
                        maxLines = descriptionMaxLines,
                        overflow = TextOverflow.Ellipsis
                    )
                    SecondaryData(uiState)
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
                        }.padding(start = mediumSpace)
                    )

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
        fontSize = extraLargeFont,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = largeSpace)
    )
}

@Composable
fun SecondaryData(game: GameDetailsState) {
    Section {
        Data(
            title = stringResource(Res.string.developers),
            value = game.developers.joinToString(", ") { it.name },
        )
        Data(
            title = stringResource(Res.string.website),
            value = game.website,
        )
        Data(
            title = stringResource(Res.string.platforms),
            value = game.platforms.joinToString(", ") { it.name }
        )
        Data(
            title = stringResource(Res.string.genres),
            value = game.genres.joinToString(", ") { it.name }
        )
    }
}

@Composable
fun Section(name: String = "", block: @Composable () -> Unit) {
    if(name.isNotEmpty()) {
        Text(
            text = name,
            fontSize = extraLargeFont,
            modifier = Modifier.padding(bottom = extraLargeSpace, start = mediumSpace)
        )
    }
    block()
}

@Composable
fun Data(title: String, value: String) {
    Column(
        Modifier.padding(start = mediumSpace, end = mediumSpace),
        Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = largeFont, fontWeight = FontWeight.Bold)
        Text(
            text = value.ifEmpty { stringResource(Res.string.no_data) },
            modifier = Modifier.padding(bottom = mediumSpace)
        )
    }
}

private const val descriptionMaxLines = 10