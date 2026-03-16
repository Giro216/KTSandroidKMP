package com.example.kts_android_kmp.feature.bootstrap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kts_android_kmp.storage.domain.ISessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


class BootstrapViewModel(
    private val sessionRepository: ISessionRepository,
) : ViewModel() {

    val state: StateFlow<BootstrapState> = combine(
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
}

