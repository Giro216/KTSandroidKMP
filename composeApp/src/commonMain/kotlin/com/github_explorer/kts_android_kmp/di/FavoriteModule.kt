package com.github_explorer.kts_android_kmp.di

import com.github_explorer.kts_android_kmp.feature.favorites.domain.usecase.ObserveFavoritesUseCase
import com.github_explorer.kts_android_kmp.feature.favorites.domain.usecase.ToggleFavoriteUseCase
import org.koin.dsl.module

val favoriteModule = module {
    factory { ObserveFavoritesUseCase(repository = get()) }
    factory { ToggleFavoriteUseCase(repository = get()) }
}