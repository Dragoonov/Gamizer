package com.moonfly.gamizer

import com.moonfly.gamizer.model.Preferences
import com.moonfly.gamizer.repository.PreferencesRepository
import com.moonfly.gamizer.repository.Response
import com.moonfly.gamizer.repository.UserPreferencesDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PreferencesRepositoryTest {

    private lateinit var repository: PreferencesRepository

    private val userPreferencesDataSource: UserPreferencesDataSource = mockk()

    @BeforeTest
    fun setup() {
        repository = PreferencesRepository(userPreferencesDataSource)
    }

    @Test
    fun `getPreferences should call getPreferences on userPreferencesDataSource`() {
        // Given
        val flow = flowOf(Response.Success(Preferences(darkMode = true)))
        every { userPreferencesDataSource.getPreferences() } returns flow

        // When
        val result = repository.getPreferences()

        // Then
        verify { userPreferencesDataSource.getPreferences() }
        assertEquals(flow, result)
    }

    @Test
    fun `changeDarkMode should call changeDarkMode on userPreferencesDataSource`() = runBlocking {
        // Given
        val isOn = true
        coEvery { userPreferencesDataSource.changeDarkMode(any()) } returns Response.Success(Unit)

        // When
        repository.changeDarkMode(isOn)

        // Then
        coVerify { userPreferencesDataSource.changeDarkMode(isOn) }

    }
}