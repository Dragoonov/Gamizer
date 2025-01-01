package com.moonfly.gamizer.base

import com.moonfly.gamizer.MainDispatcherRule
import com.moonfly.gamizer.model.Preferences
import com.moonfly.gamizer.repository.Response
import com.moonfly.gamizer.usecase.GetPreferencesFlowUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import kotlin.test.Test

class MainViewModelTest {
    private val getPreferencesFlowUseCase: GetPreferencesFlowUseCase = mockk()
    private lateinit var viewModel: MainViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should initialize preferences flow on creation`() {
        // Given
        val preferences = Preferences(darkMode = true)
        every { getPreferencesFlowUseCase() } returns flowOf(Response.Success(preferences))

        //When
        viewModel = MainViewModel(getPreferencesFlowUseCase)

        //Then
        assertTrue(viewModel.uiState.value.darkMode)
        verify { getPreferencesFlowUseCase() }
    }

}