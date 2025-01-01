package com.moonfly.gamizer.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.moonfly.gamizer.GamizerDB
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class UserPreferencesDataSourceImplTest {

    private lateinit var userPreferencesDataSource: UserPreferencesDataSourceImpl
    private val gamizerDb: GamizerDB = mockk()
    private val dataStore: DataStore<Preferences> = mockk()

    @BeforeTest
    fun setup() {
        userPreferencesDataSource = UserPreferencesDataSourceImpl(gamizerDb, dataStore)
    }

    @Test
    fun `isGameLiked should return true if game is liked`() = runTest {
        // Given
        val gameId = 1L
        every { gamizerDb.gamizerDBQueries.select(gameId).executeAsOneOrNull() } returns gameId

        // When
        val result = userPreferencesDataSource.isGameLiked(gameId.toInt())

        // Then
        assertEquals(Response.Success(true), result)
        verify { gamizerDb.gamizerDBQueries.select(gameId).executeAsOneOrNull() }
    }

    @Test
    fun `isGameLiked should return false if game is not liked`() = runTest {
        // Given
        val gameId = 1L
        every { gamizerDb.gamizerDBQueries.select(gameId).executeAsOneOrNull() } returns null

        // When
        val result = userPreferencesDataSource.isGameLiked(gameId.toInt())

        // Then
        assertEquals(Response.Success(false), result)
        verify { gamizerDb.gamizerDBQueries.select(gameId).executeAsOneOrNull() }
    }

    @Test
    fun `isGameLiked should return SerializationError when exception occurs`() = runTest {
        // Given
        val gameId = 1L
        every { gamizerDb.gamizerDBQueries.select(gameId).executeAsOneOrNull() } throws IllegalStateException()

        // When
        val result = userPreferencesDataSource.isGameLiked(gameId.toInt())

        // Then
        assertEquals(Response.Error.SerializationError, result)
        verify { gamizerDb.gamizerDBQueries.select(gameId).executeAsOneOrNull() }
    }

    @Test
    fun `changeGameLike should add game when liked is true`() = runTest {
        // Given
        val gameId = 1L
        every { gamizerDb.gamizerDBQueries.add(gameId) } just Runs

        // When
        val result = userPreferencesDataSource.changeGameLike(gameId.toInt(), liked = true)

        // Then
        assertEquals(Response.Success(Unit), result)
        verify { gamizerDb.gamizerDBQueries.add(gameId) }
    }

    @Test
    fun `changeGameLike should delete game when liked is false`() = runTest {
        // Given
        val gameId = 1L
        every { gamizerDb.gamizerDBQueries.delete(gameId) } just Runs

        // When
        val result = userPreferencesDataSource.changeGameLike(gameId.toInt(), liked = false)

        // Then
        assertEquals(Response.Success(Unit), result)
        verify { gamizerDb.gamizerDBQueries.delete(gameId) }
    }

    @Test
    fun `getPreferences should return datastore preferences`() = runTest {
        // Given
        val preferences = mockk<Preferences>()
        every { dataStore.data } returns flowOf(preferences)
        every { preferences[any<Preferences.Key<Boolean>>()] } returns true

        // When
        val result = userPreferencesDataSource.getPreferences()

        // Then
        verify { dataStore.data }
        assertTrue((result.first() as? Response.Success)?.body?.darkMode ?: false)
    }

    @Test
    fun `changeDarkMode should update datastore preferences`() = runTest {
        // Given
        val isOn = true
        val preferences = mockk<Preferences>()
        coEvery { dataStore.updateData(any()) } returns preferences

        // When
        val result = userPreferencesDataSource.changeDarkMode(isOn)

        // Then
        coVerify { dataStore.updateData(any()) }
        assertEquals(Response.Success(Unit), result)
    }
}