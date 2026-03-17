package com.example.kts_android_kmp.feature.profile.presentation

import androidx.lifecycle.viewModelScope
import com.example.kts_android_kmp.common.BaseViewModel
import com.example.kts_android_kmp.feature.profile.domain.IProfileRepository
import com.example.kts_android_kmp.feature.profile.domain.LogoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: IProfileRepository,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel<ProfileUiEvent, ProfileUiState>(ProfileUiState()) {

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, isError = false) }

            val result = repository.loadProfile()
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

