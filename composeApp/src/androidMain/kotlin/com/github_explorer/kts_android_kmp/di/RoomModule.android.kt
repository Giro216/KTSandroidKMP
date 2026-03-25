package com.github_explorer.kts_android_kmp.di

import android.content.Context
import androidx.room.Room
import com.github_explorer.kts_android_kmp.db.AppDatabase
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

//    factory { get<AppDatabase>().gitHubRepoDao() }

    factory { get<AppDatabase>().gitHubSearchCacheDao() }

    factory<GitHubRepoCache> { RoomGitHubSearchCacheImpl(dao = get()) }

    factory<AppDataCleaner> {
        AndroidAppDataCleanerImpl(
            db = get(),
            sessionRepository = get(),
        )
    }
}

