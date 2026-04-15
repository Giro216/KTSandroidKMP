package com.github_explorer.kts_android_kmp.feature.settings.domain.useCase

import com.github_explorer.kts_android_kmp.feature.settings.domain.SettingsRepository

class SetLanguageUseCase(
    private val settingsRepository: SettingsRepository,
) {
    fun observeCurrentLanguage() = settingsRepository.observeLanguage()

    suspend fun execute(language: String) {
        settingsRepository.setLanguage(language)
    }
}