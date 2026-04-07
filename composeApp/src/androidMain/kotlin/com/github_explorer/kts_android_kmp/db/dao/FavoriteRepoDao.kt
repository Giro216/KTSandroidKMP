package com.github_explorer.kts_android_kmp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github_explorer.kts_android_kmp.db.entity.FavoriteRepoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRepoDao {

    @Query("SELECT * FROM ${FavoriteRepoEntity.TABLE} ORDER BY savedAt DESC")
    fun observeAll(): Flow<List<FavoriteRepoEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM ${FavoriteRepoEntity.TABLE} WHERE repoId = :repoId)")
    suspend fun exists(repoId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: FavoriteRepoEntity)

    @Query("DELETE FROM ${FavoriteRepoEntity.TABLE} WHERE repoId = :repoId")
    suspend fun delete(repoId: Long)
}

