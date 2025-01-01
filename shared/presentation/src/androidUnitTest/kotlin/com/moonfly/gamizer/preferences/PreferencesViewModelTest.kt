package com.moonfly.gamizer.preferences

import com.moonfly.gamizer.MainDispatcherRule
import com.moonfly.gamizer.model.Preferences
import com.moonfly.gamizer.repository.Response
import com.moonfly.gamizer.usecase.ChangeDarkModeUseCase
import com.moonfly.gamizer.usecase.GetPreferencesFlowUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class PreferencesViewModelTest {
    private val getPreferencesFlowUseCase: GetPreferencesFlowUseCase = mockk()
    private val changeDarkModeUseCase: ChangeDarkModeUseCase = mockk()
    private lateinit var viewModel: PreferencesViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        every { getPreferencesFlowUseCase() } returns flowOf(Response.Success(Preferences(darkMode = true)))
        viewModel = PreferencesViewModel(getPreferencesFlowUseCase, changeDarkModeUseCase)
    }

    @Test
    fun `should load preferences on creation`() {
        // Then
        assertTrue(viewModel.uiState.value.darkMode)
        verify { getPreferencesFlowUseCase() }
    }

    @Test
    fun `should load preferences on refresh`() {
        // When
        viewModel.handleEvent(PreferencesEvent.OnRefresh)

        // Then
        verify(exactly = 2) { getPreferencesFlowUseCase() }
    }

    @Test
    fun `should change dark mode on event`() = runBlocking {
        // When
        viewModel.handleEvent(PreferencesEvent.OnDarkModeChanged(false))

        // Then
        assertFalse(viewModel.uiState.value.darkMode)
        coVerify { changeDarkModeUseCase(false) }
    }
}