package com.moonfly.gamizer.preferences

import androidx.lifecycle.viewModelScope
import com.moonfly.gamizer.base.BaseViewModel
import com.moonfly.gamizer.base.UIEvent
import com.moonfly.gamizer.base.UIState
import com.moonfly.gamizer.repository.Response
import com.moonfly.gamizer.usecase.ChangeDarkModeUseCase
import com.moonfly.gamizer.usecase.GetPreferencesFlowUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PreferencesViewModel(
    private val getPreferencesUseCase: GetPreferencesFlowUseCase,
    private val changeDarkModeUseCase: ChangeDarkModeUseCase
) : BaseViewModel<PreferencesEvent, PreferencesState, Nothing>(PreferencesState()) {

    private var preferencesJob: Job? = null

    init {
        loadPreferences()
    }

    override fun handleEvent(event: PreferencesEvent) {
        when (event) {
            is PreferencesEvent.OnDarkModeChanged -> {
                changeDarkMode(event.isOn)
            }
            is PreferencesEvent.OnRefresh -> {
                loadPreferences()
            }
        }
    }

    private fun loadPreferences() {
        preferencesJob?.cancel()
        preferencesJob = viewModelScope.launch {
            getPreferencesUseCase().onEach {
                when (it) {
                    is Response.Success -> updateState {
                        copy(
                            isError = false,
                            darkMode = it.body.darkMode,
                            isLoading = false
                        )
                    }
                    is Response.Error -> updateState { copy(isError = true, isLoading = false) }
                }
            }.stateIn(viewModelScope)
        }
    }

    private fun changeDarkMode(isOn: Boolean) {
        updateState { copy(darkMode = isOn) }
        viewModelScope.launch {
            changeDarkModeUseCase(isOn)
        }
    }

}

data class PreferencesState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val darkMode: Boolean = false
) : UIState

sealed class PreferencesEvent : UIEvent {
    data class OnDarkModeChanged(val isOn: Boolean) : PreferencesEvent()
    data object OnRefresh : PreferencesEvent()
}