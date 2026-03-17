package com.example.kts_android_kmp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kts_android_kmp.db.entity.GitHubRepoCacheEntity

@Dao
interface GitHubRepoDao {

    @Query("SELECT * FROM ${GitHubRepoCacheEntity.TABLE} WHERE query = :query ORDER BY position ASC")
    suspend fun getByQuery(query: String): List<GitHubRepoCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<GitHubRepoCacheEntity>)

    @Query("DELETE FROM ${GitHubRepoCacheEntity.TABLE} WHERE query = :query")
    suspend fun deleteQuery(query: String)
}

