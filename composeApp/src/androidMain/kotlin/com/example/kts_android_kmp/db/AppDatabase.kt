package com.example.kts_android_kmp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kts_android_kmp.db.dao.GitHubRepoDao
import com.example.kts_android_kmp.db.dao.GitHubSearchCacheDao
import com.example.kts_android_kmp.db.entity.GitHubRepoCacheEntity
import com.example.kts_android_kmp.db.entity.GitHubRepoEntity
import com.example.kts_android_kmp.db.entity.GitHubSearchQueryEntity
import com.example.kts_android_kmp.db.entity.GitHubSearchResultEntity

@Database(
    entities = [
        // legacy (можно удалить позже)
        GitHubRepoCacheEntity::class,

        // normalized cache
        GitHubRepoEntity::class,
        GitHubSearchQueryEntity::class,
        GitHubSearchResultEntity::class,
    ],
    version = 3,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gitHubRepoDao(): GitHubRepoDao

    abstract fun gitHubSearchCacheDao(): GitHubSearchCacheDao

    companion object {
        const val NAME: String = "app.db"
    }
}

