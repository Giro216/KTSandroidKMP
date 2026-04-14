package com.github_explorer.kts_android_kmp.di

import android.content.Context
import androidx.room.Room
import com.github_explorer.kts_android_kmp.db.AppDatabase
import com.github_explorer.kts_android_kmp.feature.favorites.data.RoomFavoriteRepository
import com.github_explorer.kts_android_kmp.feature.favorites.data.SystemTimeProvider
import com.github_explorer.kts_android_kmp.feature.favorites.data.TimeProvider
import com.github_explorer.kts_android_kmp.feature.favorites.domain.FavoriteRepository
import com.github_explorer.kts_android_kmp.feature.favorites.domain.usecase.ObserveFavoritesUseCase
import com.github_explorer.kts_android_kmp.feature.favorites.domain.usecase.ToggleFavoriteUseCase
import com.github_explorer.kts_android_kmp.feature.mainScreen.cache.RoomGitHubSearchCacheImpl
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.cache.GitHubRepoCache
import com.github_explorer.kts_android_kmp.feature.profile.data.AndroidAppDataCleanerImpl
import com.github_explorer.kts_android_kmp.feature.profile.platform.AppDataCleaner
import org.koin.dsl.module

fun roomModule(context: Context) = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            AppDatabase.NAME,
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    factory { get<AppDatabase>().gitHubSearchCacheDao() }
    factory<GitHubRepoCache> { RoomGitHubSearchCacheImpl(dao = get()) }

    // favorites
    factory { get<AppDatabase>().favoriteRepoDao() }
    single<TimeProvider> { SystemTimeProvider() }
    single<FavoriteRepository> { RoomFavoriteRepository(dao = get(), timeProvider = get()) }
    factory { ObserveFavoritesUseCase(repository = get()) }
    factory { ToggleFavoriteUseCase(repository = get()) }

    factory<AppDataCleaner> {
        AndroidAppDataCleanerImpl(
            db = get(),
            sessionRepository = get(),
        )
    }
}
