package com.github_explorer.kts_android_kmp.feature.profile.userRepoScreen.presentation

import androidx.lifecycle.viewModelScope
import com.github_explorer.kts_android_kmp.common.BaseViewModel
import com.github_explorer.kts_android_kmp.core.data.storage.domain.SessionRepository
import com.github_explorer.kts_android_kmp.feature.profile.userRepoScreen.domain.useCase.LoadUserRepoUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserRepoViewModel(
    private val loadUseCase: LoadUserRepoUseCase,
    private val sessionRepository: SessionRepository,
) : BaseViewModel<UserRepoUiEvent, UserRepoUiState>(UserRepoUiState()) {

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            val hasToken = sessionRepository.accessToken().first().orEmpty().isNotBlank()
            if (!hasToken) {
                updateState { copy(isLoading = false, isError = false, repos = emptyList()) }
                return@launch
            }

            updateState { copy(isLoading = true, isError = false) }

            loadUseCase.loadUserRepos()
                .onSuccess { repos ->
                    updateState { copy(isLoading = false, isError = false, repos = repos) }
                }
                .onFailure {
                    updateState { copy(isLoading = false, isError = true, repos = emptyList()) }
                }
        }
    }
}