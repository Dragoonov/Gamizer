package com.moonfly.gamizer.gamelist

import androidx.lifecycle.viewModelScope
import com.moonfly.gamizer.base.BaseViewModel
import com.moonfly.gamizer.base.UIEffect
import com.moonfly.gamizer.base.UIEvent
import com.moonfly.gamizer.base.UIState
import com.moonfly.gamizer.model.Game
import com.moonfly.gamizer.repository.Response
import com.moonfly.gamizer.usecase.GetGamesUseCase
import kotlinx.coroutines.launch

class GameListViewModel(
    private val getGamesUseCase: GetGamesUseCase,
) : BaseViewModel<GameListEvent, GameListState, GameListEffect>(GameListState()) {

    init {
        reloadGames()
    }

    override fun handleEvent(event: GameListEvent) {
        when (event) {
            is GameListEvent.OnRefresh -> {
                reloadGames()
            }

            is GameListEvent.OnGameClicked -> sendEffect(GameListEffect.NavigateToDetails(event.id))
        }
    }

    private fun reloadGames() {
        updateState { copy(isLoading = true, isError = false) }
        viewModelScope.launch {
            val games = getGamesUseCase(INITIAL_PAGE)
            if (games is Response.Error) {
                updateState { copy(isError = true, isLoading = false) }
                return@launch
            }
            updateState {
                copy(
                    isLoading = false,
                    isError = false,
                    games = (games as Response.Success).body
                )
            }
        }
    }

    private companion object {
        private const val INITIAL_PAGE = 1
    }
}

data class GameListState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val games: List<Game> = emptyList()
) : UIState

sealed class GameListEvent : UIEvent {
    data object OnRefresh : GameListEvent()
    data class OnGameClicked(val id: Int) : GameListEvent()
}

sealed class GameListEffect : UIEffect {
    data class NavigateToDetails(val id: Int) : GameListEffect()
}