package com.zaharinskijvlad.cryptowallet.presentation.utils

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class StatefulViewModel<State : Any>(
    initialState: State,
) : ViewModel() {
    private val _stateFlow by lazy { MutableStateFlow(initialState) }

    val stateFlow: StateFlow<State> get() = _stateFlow.asStateFlow()

    val state: State get() = stateFlow.value

    fun updateState(transform: State.() -> State) {
        _stateFlow.value = transform.invoke(state)
    }
}