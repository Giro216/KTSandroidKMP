package com.github_explorer.kts_android_kmp.feature.settings.data.repo

import com.github_explorer.kts_android_kmp.core.data.storage.domain.AppPreferences
import com.github_explorer.kts_android_kmp.core.data.storage.domain.PrefKeys
import com.github_explorer.kts_android_kmp.feature.settings.domain.SettingsRepository
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val appPreferences: AppPreferences,
) : SettingsRepository {
    override fun observeDarkTheme() =
        appPreferences.getBoolean(PrefKeys.IS_DARK_THEME, default = false)

    override fun observeLanguage() =
        appPreferences.getString(PrefKeys.LANGUAGE, default = "ru-RU").map { it ?: "ru-RU" }

    override suspend fun setDarkTheme(isDarkTheme: Boolean) {
        appPreferences.putBoolean(PrefKeys.IS_DARK_THEME, isDarkTheme)
    }

    override suspend fun setLanguage(language: String) {
        appPreferences.putString(PrefKeys.LANGUAGE, language)
    }
}