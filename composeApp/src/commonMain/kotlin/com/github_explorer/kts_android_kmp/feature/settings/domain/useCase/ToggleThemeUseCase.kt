package com.github_explorer.kts_android_kmp.feature.settings.domain.useCase

import com.github_explorer.kts_android_kmp.feature.settings.domain.SettingsRepository
import kotlinx.coroutines.flow.first

class ToggleThemeUseCase(
    private val settingsRepository: SettingsRepository,
) {
    fun observeCurrentTheme() = settingsRepository.observeDarkTheme()

    suspend fun execute() {
        val current = settingsRepository.observeDarkTheme().first()
        settingsRepository.setDarkTheme(!current)
    }
}