package com.moonfly.gamizer.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<Event : UIEvent, State : UIState, Effect : UIEffect>(initialUIState: State): ViewModel() {

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialUIState)
    val uiState = _uiState.asStateFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()


    abstract fun handleEvent(event: Event)

    protected fun updateState(transform: State.() -> State) {
        _uiState.value = _uiState.value.transform()
    }

    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }

}