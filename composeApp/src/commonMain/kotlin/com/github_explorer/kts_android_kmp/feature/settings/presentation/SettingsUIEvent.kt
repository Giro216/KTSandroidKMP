package com.github_explorer.kts_android_kmp.feature.settings.presentation

sealed interface SettingsUIEvent {
    data object ToggleTheme : SettingsUIEvent

    data class SetLanguage(val language: String) : SettingsUIEvent
}