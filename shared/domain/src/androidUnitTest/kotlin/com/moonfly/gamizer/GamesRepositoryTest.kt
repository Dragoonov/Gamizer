package com.moonfly.gamizer

import com.moonfly.gamizer.model.Game
import com.moonfly.gamizer.repository.GamesDataSource
import com.moonfly.gamizer.repository.GamesRepository
import com.moonfly.gamizer.repository.Response
import com.moonfly.gamizer.repository.UserPreferencesDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GamesRepositoryTest {

    private val gamesDataSource: GamesDataSource = mockk()
    private val userPreferencesDataSource: UserPreferencesDataSource = mockk()
    private lateinit var repository: GamesRepository

    @BeforeTest
    fun setup() {
        repository = GamesRepository(gamesDataSource, userPreferencesDataSource)
    }

    @Test
    fun `getGames should call getGames on gamesDataSource`() = runTest {
        // Given
        val page = 1
        val mockGames = Response.Success(listOf(Game(id = 1), Game(id = 2)))
        coEvery { gamesDataSource.getGames(page) } returns mockGames

        // When
        val result = repository.getGames(page)

        // Then
        coVerify { gamesDataSource.getGames(page) }
        assertEquals(mockGames, result)
    }

    @Test
    fun `getGameDetails should call getGameDetails on gamesDataSource`() = runTest {
        // Given
        val gameId = 1
        val mockGameDetails = Response.Success(Game(id = gameId))
        coEvery { gamesDataSource.getGameDetails(gameId) } returns mockGameDetails

        // When
        val result = repository.getGameDetails(gameId)

        // Then
        coVerify { gamesDataSource.getGameDetails(gameId) }
        assertEquals(mockGameDetails, result)
    }

    @Test
    fun `isGameLiked should call isGameLiked on userPreferencesDataSource`() = runTest {
        // Given
        val gameId = 1
        val isLiked = Response.Success(true )
        coEvery { userPreferencesDataSource.isGameLiked(gameId) } returns isLiked

        // When
        val result = repository.isGameLiked(gameId)

        // Then
        coVerify { userPreferencesDataSource.isGameLiked(gameId) }
        assertEquals(isLiked, result)
    }

    @Test
    fun `changeGameLike should call changeGameLike on userPreferencesDataSource`() = runTest {
        // Given
        val gameId = 1
        val liked = true
        val isSuccess = Response.Success(Unit)
        coEvery { userPreferencesDataSource.changeGameLike(gameId, liked) } returns isSuccess

        // When
        repository.changeGameLike(gameId, liked)

        // Then
        coVerify { userPreferencesDataSource.changeGameLike(gameId, liked) }
    }
}