package com.github_explorer.kts_android_kmp.feature.settings.domain

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
	fun observeDarkTheme(): Flow<Boolean>

	fun observeLanguage(): Flow<String>

	suspend fun setDarkTheme(isDarkTheme: Boolean)

	suspend fun setLanguage(language: String)
}