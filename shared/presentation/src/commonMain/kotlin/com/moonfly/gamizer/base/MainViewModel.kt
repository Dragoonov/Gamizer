package com.moonfly.gamizer.base

import androidx.lifecycle.viewModelScope
import com.moonfly.gamizer.repository.Response
import com.moonfly.gamizer.usecase.GetPreferencesFlowUseCase
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val getPreferencesFlowUseCase: GetPreferencesFlowUseCase,
) : BaseViewModel<Nothing, PreferencesState, Nothing>(PreferencesState()) {

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            getPreferencesFlowUseCase()
                .onEach {
                    when (it) {
                        is Response.Success -> updateState {
                            copy(darkMode = it.body.darkMode, isLoading = false)
                        }
                        is Response.Error -> updateState { copy(darkMode = false, isLoading = false) }
                    }
                }
                .stateIn(viewModelScope)
        }
    }

    override fun handleEvent(event: Nothing) {}

}

data class PreferencesState(
    val isLoading: Boolean = true,
    val darkMode: Boolean = false
) : UIState