package com.example.kts_android_kmp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.kts_android_kmp.db.entity.GitHubRepoEntity
import com.example.kts_android_kmp.db.entity.GitHubSearchQueryEntity
import com.example.kts_android_kmp.db.entity.GitHubSearchResultEntity

@Dao
interface GitHubSearchCacheDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuery(entity: GitHubSearchQueryEntity): Long

    @Query("SELECT id FROM ${GitHubSearchQueryEntity.TABLE} WHERE queryNormalized = :queryNormalized")
    suspend fun getQueryId(queryNormalized: String): Long?

    @Query("DELETE FROM ${GitHubSearchQueryEntity.TABLE} WHERE queryNormalized = :queryNormalized")
    suspend fun deleteQuery(queryNormalized: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRepos(repos: List<GitHubRepoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSearchResults(results: List<GitHubSearchResultEntity>)

    @Query("DELETE FROM ${GitHubSearchResultEntity.TABLE} WHERE queryId = :queryId")
    suspend fun deleteSearchResults(queryId: Long)

    @Transaction
    suspend fun replaceQueryResults(
        queryId: Long,
        repos: List<GitHubRepoEntity>,
        results: List<GitHubSearchResultEntity>,
    ) {
        upsertRepos(repos)
        deleteSearchResults(queryId)
        upsertSearchResults(results)
    }

    @Query(
        """
        SELECT r.*
        FROM ${GitHubRepoEntity.TABLE} r
        INNER JOIN ${GitHubSearchResultEntity.TABLE} s
            ON s.repoId = r.repoId AND s.queryId = :queryId
        ORDER BY s.position ASC
        """
    )
    suspend fun getReposByQueryId(queryId: Long): List<GitHubRepoEntity>
}


