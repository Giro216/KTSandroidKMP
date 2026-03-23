package com.example.kts_android_kmp.di

import android.content.Context
import androidx.room.Room
import com.example.kts_android_kmp.db.AppDatabase
import com.example.kts_android_kmp.feature.mainScreen.cache.RoomGitHubRepoCache
import com.example.kts_android_kmp.feature.mainScreen.domain.cache.IGitHubRepoCache
import com.example.kts_android_kmp.feature.profile.data.AndroidAppDataCleaner
import com.example.kts_android_kmp.feature.profile.domain.IAppDataCleaner
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

    factory { get<AppDatabase>().gitHubRepoDao() }

    factory<IGitHubRepoCache> { RoomGitHubRepoCache(dao = get()) }

    factory<IAppDataCleaner> {
        AndroidAppDataCleaner(
            db = get(),
            sessionRepository = get(),
        )
    }
}

