package com.example.kts_android_kmp.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<Label, State>(initialState: State) : ViewModel() {

    private val mutableLabel = Channel<Label>(Channel.BUFFERED)

    private val mutableState = MutableStateFlow(initialState)

    val state: StateFlow<State>
        get() = mutableState.asStateFlow()

     val label: Flow<Label>
        get() = mutableLabel.receiveAsFlow()


    fun updateState(block: State.() -> State) {
        mutableState.update { it.block() }
    }

    protected open fun acceptLabel(label: Label) {
        viewModelScope.launch {
            mutableLabel.send(label)
        }
    }
}

