package com.moonfly.gamizer.gamedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.moonfly.gamizer.base.BaseViewModel
import com.moonfly.gamizer.base.UIEvent
import com.moonfly.gamizer.base.UIState
import com.moonfly.gamizer.model.Developer
import com.moonfly.gamizer.model.Genre
import com.moonfly.gamizer.model.Platform
import com.moonfly.gamizer.repository.Response
import com.moonfly.gamizer.usecase.GetGameDetailsUseCase
import com.moonfly.gamizer.usecase.LikeGameUseCase
import com.moonfly.gamizer.usecase.IsGameLikedUseCase
import kotlinx.coroutines.launch

class GameDetailsViewModel(
    private val isGameLikeGameUseCase: IsGameLikedUseCase,
    private val likeGameUseCase: LikeGameUseCase,
    private val getGameDetailsUseCase: GetGameDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<GameDetailsEvent, GameDetailsState, Nothing>(
    GameDetailsState()
) {

    private val gameId = savedStateHandle.get<Int>(GAME_ID_KEY) ?: 0

    init {
        reloadDetails()
    }

    override fun handleEvent(event: GameDetailsEvent) {
        when (event) {
            is GameDetailsEvent.OnGameLikeChanged -> {
                viewModelScope.launch {
                    val isSuccessResponse = likeGameUseCase(event.gameId, event.isLiked)
                    if (isSuccessResponse is Response.Error) {
                        updateState { copy(isError = true) }
                        return@launch
                    }
                    updateState { copy(isLiked = event.isLiked) }
                }
            }
            is GameDetailsEvent.OnRefresh -> {
                reloadDetails()
            }
        }
    }

    private fun reloadDetails() {
        updateState { copy(isLoading = true, isError = false) }
        viewModelScope.launch {
            val isLikedResponse = isGameLikeGameUseCase(gameId)
            val gameResponse = getGameDetailsUseCase(gameId)
            if (gameResponse is Response.Error || isLikedResponse is Response.Error) {
                updateState { copy(isError = true, isLoading = false) }
                return@launch
            }
            val game = (gameResponse as Response.Success).body
            val isLiked = (isLikedResponse as Response.Success).body
            updateState {
                copy(
                    isLoading = false,
                    isError = false,
                    id = game.id,
                    title = game.title,
                    imageUrl = game.imageUrl,
                    description = game.description,
                    website = game.website,
                    isLiked = isLiked,
                    genres = game.genres,
                    developers = game.developers,
                    platforms = game.platforms
                )
            }
        }
    }

    companion object {
        const val GAME_ID_KEY = "gameId"
    }

}

sealed class GameDetailsEvent : UIEvent {
    data class OnGameLikeChanged(val gameId: Int, val isLiked: Boolean) : GameDetailsEvent()
    data object OnRefresh : GameDetailsEvent()
}

data class GameDetailsState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val id: Int = 0,
    val title: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val website: String = "",
    val developers: List<Developer> = emptyList(),
    val platforms: List<Platform> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val isLiked: Boolean = false
) : UIState