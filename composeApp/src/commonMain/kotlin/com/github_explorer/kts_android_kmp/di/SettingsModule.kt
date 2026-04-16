package com.github_explorer.kts_android_kmp.di

import com.github_explorer.kts_android_kmp.feature.settings.data.repo.SettingsRepositoryImpl
import com.github_explorer.kts_android_kmp.feature.settings.domain.SettingsRepository
import com.github_explorer.kts_android_kmp.feature.settings.domain.useCase.SetLanguageUseCase
import com.github_explorer.kts_android_kmp.feature.settings.domain.useCase.ToggleThemeUseCase
import com.github_explorer.kts_android_kmp.feature.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single<SettingsRepository> { SettingsRepositoryImpl(appPreferences = get()) }
    factory { ToggleThemeUseCase(settingsRepository = get()) }
    factory { SetLanguageUseCase(settingsRepository = get()) }

    viewModel {
        SettingsViewModel(
            toggleThemeUseCase = get(),
            setLanguageUseCase = get(),
        )
    }
}

