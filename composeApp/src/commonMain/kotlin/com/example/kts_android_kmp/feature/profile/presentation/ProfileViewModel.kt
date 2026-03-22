package com.example.kts_android_kmp.feature.profile.presentation

import androidx.lifecycle.viewModelScope
import com.example.kts_android_kmp.common.BaseViewModel
import com.example.kts_android_kmp.feature.profile.domain.useCase.LoadUseCase
import com.example.kts_android_kmp.feature.profile.domain.useCase.LogoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val loadUseCase: LoadUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel<ProfileUiEvent, ProfileUiState>(ProfileUiState()) {

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
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

    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                logoutUseCase.logout()
            }
            acceptLabel(ProfileUiEvent.LogoutSuccess)
        }
    }
}

