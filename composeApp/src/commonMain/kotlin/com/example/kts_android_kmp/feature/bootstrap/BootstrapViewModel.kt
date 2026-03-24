package com.example.kts_android_kmp.feature.bootstrap

import androidx.lifecycle.viewModelScope
import com.example.kts_android_kmp.common.BaseViewModel
import com.example.kts_android_kmp.core.data.storage.domain.SessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class BootstrapViewModel(
    private val sessionRepository: SessionRepository,

    ) : BaseViewModel<BootstrapUiEvent, BootstrapState>(
    BootstrapState(isLoading = true),
    extraBufferCapacity = 1
) {

    private val sessionState: StateFlow<BootstrapState> = combine(
        sessionRepository.onboardingShown(),
        sessionRepository.isLoggedIn(),
    ) { onboardingShown, isLoggedIn ->
        BootstrapState(
            isLoading = false,
            onboardingShown = onboardingShown,
            isLoggedIn = isLoggedIn,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3_000),
        initialValue = BootstrapState(isLoading = true),
    )

    init {
        viewModelScope.launch {
            sessionState.collect { s ->
                updateState { s }

                if (!s.isLoading) {
                    if (!s.onboardingShown && !s.isLoggedIn) {
                        acceptLabel(BootstrapUiEvent.NavigateToHello)
                    } else {
                        acceptLabel(BootstrapUiEvent.NavigateToMain)
                    }
                }
            }
        }
    }
}

