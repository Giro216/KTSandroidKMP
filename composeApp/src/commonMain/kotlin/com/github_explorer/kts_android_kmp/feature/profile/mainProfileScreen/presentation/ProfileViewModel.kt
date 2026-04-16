package com.github_explorer.kts_android_kmp.feature.profile.mainProfileScreen.presentation

import androidx.lifecycle.viewModelScope
import com.github_explorer.kts_android_kmp.common.BaseViewModel
import com.github_explorer.kts_android_kmp.core.data.storage.domain.SessionRepository
import com.github_explorer.kts_android_kmp.feature.profile.mainProfileScreen.domain.useCase.LoadUseCase
import com.github_explorer.kts_android_kmp.feature.settings.domain.useCase.LogoutUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val loadUseCase: LoadUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val sessionRepository: SessionRepository,
) : BaseViewModel<ProfileUiEvent, ProfileUiState>(ProfileUiState()) {

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            val hasToken = sessionRepository.accessToken().first().orEmpty().isNotBlank()
            if (!hasToken) {
                updateState { copy(isLoading = false, profile = null, isError = false) }
                return@launch
            }

            updateState { copy(isLoading = true, isError = false) }

            val result = loadUseCase.loadProfile()
            result
                .onSuccess { profile ->
                    updateState { copy(isLoading = false, profile = profile, isError = false) }
                }
                .onFailure {
                    updateState { copy(isLoading = false, profile = null, isError = true) }
                }
        }
    }
}

