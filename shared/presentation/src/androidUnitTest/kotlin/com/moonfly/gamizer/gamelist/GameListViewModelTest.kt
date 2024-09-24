package com.moonfly.gamizer.gamelist

import com.moonfly.gamizer.MainDispatcherRule
import com.moonfly.gamizer.model.Game
import com.moonfly.gamizer.repository.Response
import com.moonfly.gamizer.usecase.GetGamesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class GameListViewModelTest {

    private val getGamesUseCase: GetGamesUseCase = mockk()
    private lateinit var viewModel: GameListViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @BeforeTest
    fun setup() {
        viewModel = GameListViewModel(getGamesUseCase)
    }

    @Test
    fun `reloadGames should load games successfully`() = runBlocking {
        // Given
        val gamesList = listOf(Game(id = 1, title = "Game 1"), Game(id = 2, title = "Game 2"))
        val response = Response.Success(gamesList)

        coEvery { getGamesUseCase(1) } returns response

        // When
        viewModel.handleEvent(GameListEvent.OnRefresh)

        // Then
        assertEquals(false, viewModel.uiState.value.isError)
        assertEquals(false, viewModel.uiState.value.isLoading)
        assertEquals(gamesList, viewModel.uiState.value.games)
        coVerify { getGamesUseCase(1) }
    }

    @Test
    fun `reloadGames should handle error when loading games fails`() = runBlocking {
        // Given
        val response = Response.Error.HttpError(500, "Internal Server Error")

        coEvery { getGamesUseCase(1) } returns response

        // When
        viewModel.handleEvent(GameListEvent.OnRefresh)

        // Then
        assertEquals(true, viewModel.uiState.value.isError)
        assertEquals(false, viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.games.isEmpty())
        coVerify { getGamesUseCase(1) }
    }

    @Test
    fun `handleEvent OnGameClicked should send NavigateToDetails effect`() = runBlocking {
        // Given
        val gameId = 1

        // When
        viewModel.handleEvent(GameListEvent.OnGameClicked(gameId))

        // Then
        val effect = viewModel.effect.first()
        assertTrue(effect is GameListEffect.NavigateToDetails)
        assertEquals(gameId, (effect as GameListEffect.NavigateToDetails).id)
    }
}