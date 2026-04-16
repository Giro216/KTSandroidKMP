package com.github_explorer.kts_android_kmp.feature.settings.presentation

import androidx.lifecycle.viewModelScope
import com.github_explorer.kts_android_kmp.common.BaseViewModel
import com.github_explorer.kts_android_kmp.feature.settings.domain.useCase.LogoutUseCase
import com.github_explorer.kts_android_kmp.feature.settings.domain.useCase.SetLanguageUseCase
import com.github_explorer.kts_android_kmp.feature.settings.domain.useCase.ToggleThemeUseCase
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val toggleThemeUseCase: ToggleThemeUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
) : BaseViewModel<SettingsUIEvent, SettingsUIState>(SettingsUIState()) {

    init {
        combine(
            toggleThemeUseCase.observeCurrentTheme().distinctUntilChanged(),
            setLanguageUseCase.observeCurrentLanguage().distinctUntilChanged(),
        ) { isDarkTheme, language ->
            SettingsUIState(
                isDarkTheme = isDarkTheme,
                language = language,
            )
        }.onEach { state ->
            updateState { state }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: SettingsUIEvent) {
        when (event) {
            is SettingsUIEvent.ToggleTheme -> {
                viewModelScope.launch {
                    toggleThemeUseCase.execute()
                }
            }

            is SettingsUIEvent.SetLanguage -> {
                viewModelScope.launch {
                    setLanguageUseCase.execute(event.language)
                }
            }

            else -> Napier.i("Logout success")
        }
    }

    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                logoutUseCase.logout()
            }
            acceptLabel(SettingsUIEvent.LogoutSuccess)
        }
    }

    fun toggleTheme() {
        onEvent(SettingsUIEvent.ToggleTheme)
    }

    fun setLanguage(language: String) {
        onEvent(SettingsUIEvent.SetLanguage(language))
    }

    fun getThemeState() = toggleThemeUseCase.observeCurrentTheme()
    fun getLanguageState() = setLanguageUseCase.observeCurrentLanguage()
}
