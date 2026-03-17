package com.example.kts_android_kmp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kts_android_kmp.db.dao.GitHubRepoDao
import com.example.kts_android_kmp.db.entity.GitHubRepoCacheEntity

@Database(
    entities = [GitHubRepoCacheEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gitHubRepoDao(): GitHubRepoDao

    companion object {
        const val NAME: String = "app.db"
    }
}

