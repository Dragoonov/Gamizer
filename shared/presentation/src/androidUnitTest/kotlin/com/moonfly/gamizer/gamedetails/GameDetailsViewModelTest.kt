package com.moonfly.gamizer.gamedetails

import androidx.lifecycle.SavedStateHandle
import com.moonfly.gamizer.MainDispatcherRule
import com.moonfly.gamizer.model.Game
import com.moonfly.gamizer.repository.Response
import com.moonfly.gamizer.usecase.GetGameDetailsUseCase
import com.moonfly.gamizer.usecase.IsGameLikedUseCase
import com.moonfly.gamizer.usecase.LikeGameUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class GameDetailsViewModelTest {

    private val isGameLikedUseCase: IsGameLikedUseCase = mockk()
    private val likeGameUseCase: LikeGameUseCase = mockk()
    private val getGameDetailsUseCase: GetGameDetailsUseCase = mockk()
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(mapOf(GameDetailsViewModel.GAME_ID_KEY to 1))
    private lateinit var viewModel: GameDetailsViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @BeforeTest
    fun setup() {
        viewModel = GameDetailsViewModel(
            isGameLikedUseCase,
            likeGameUseCase,
            getGameDetailsUseCase,
            savedStateHandle
        )
    }

    @Test
    fun `reloadDetails should load game details successfully`() = runBlocking {
        // Given
        val gameId = 1
        val gameResponse = Response.Success(Game(id = gameId, title = "Game Title"))
        val isLikedResponse = Response.Success(true)
        coEvery { getGameDetailsUseCase(gameId) } returns gameResponse
        coEvery { isGameLikedUseCase(gameId) } returns isLikedResponse

        // When
        viewModel.handleEvent(GameDetailsEvent.OnRefresh)

        // Then
        assertEquals(false, viewModel.uiState.value.isError)
        assertEquals(false, viewModel.uiState.value.isLoading)
        assertEquals("Game Title", viewModel.uiState.value.title)
        assertEquals(true, viewModel.uiState.value.isLiked)
        coVerify { getGameDetailsUseCase(gameId) }
        coVerify { isGameLikedUseCase(gameId) }
    }

    @Test
    fun `handleEvent OnGameLikeChanged should update like state successfully`() = runBlocking {
        // Given
        val gameId = 1
        val isLiked = true
        coEvery { likeGameUseCase(gameId, isLiked) } returns Response.Success(Unit)

        // When
        viewModel.handleEvent(GameDetailsEvent.OnGameLikeChanged(gameId, isLiked))

        // Then
        assertEquals(isLiked, viewModel.uiState.value.isLiked)
        coVerify { likeGameUseCase(gameId, isLiked) }
    }

    @Test
    fun `handleEvent OnGameLikeChanged should handle error`() = runBlocking {
        // Given
        val gameId = 1
        val isLiked = false
        coEvery { likeGameUseCase(gameId, isLiked) } returns Response.Error.HttpError(500, "Error")

        // When
        viewModel.handleEvent(GameDetailsEvent.OnGameLikeChanged(gameId, isLiked))

        // Then
        assertTrue(viewModel.uiState.value.isError)
        coVerify { likeGameUseCase(gameId, isLiked) }
    }

    @Test
    fun `reloadDetails should handle error when getting game details fails`() = runBlocking {
        // Given
        val gameId = 1
        val gameResponse = Response.Error.HttpError(404, "Not Found")
        val isLikedResponse = Response.Success(false)

        coEvery { getGameDetailsUseCase(gameId) } returns gameResponse
        coEvery { isGameLikedUseCase(gameId) } returns isLikedResponse

        // When
        viewModel.handleEvent(GameDetailsEvent.OnRefresh)

        // Then
        assertEquals(true, viewModel.uiState.value.isError)
        assertEquals(false, viewModel.uiState.value.isLoading)
        coVerify { getGameDetailsUseCase(gameId) }
        coVerify { isGameLikedUseCase(gameId) }
    }
}