package com.github_explorer.kts_android_kmp.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<Label, State>(initialState: State, extraBufferCapacity: Int = 0) :
    ViewModel() {

    private val mutableEvents = MutableSharedFlow<Label>(extraBufferCapacity = extraBufferCapacity)

    private val mutableState = MutableStateFlow(value = initialState)

    val state: StateFlow<State>
        get() = mutableState.asStateFlow()

    val events: SharedFlow<Label>
        get() = mutableEvents


    fun updateState(block: State.() -> State) {
        mutableState.update { it.block() }
    }

    protected open fun acceptLabel(label: Label) {
        viewModelScope.launch {
            mutableEvents.emit(label)
        }
    }
}

