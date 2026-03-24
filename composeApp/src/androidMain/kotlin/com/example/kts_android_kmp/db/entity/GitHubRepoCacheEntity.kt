package com.example.kts_android_kmp.db.entity

import androidx.room.Entity
import androidx.room.Index


@Entity(
    tableName = GitHubRepoCacheEntity.TABLE,
    primaryKeys = ["repoId"],
    indices = [Index("query")],
)
data class GitHubRepoCacheEntity(
    val query: String,
    val repoId: Long,

    val owner: String,
    val name: String,
    val description: String?,
    val language: String?,
    val stars: Int,
    val forks: Int,
    val updatedAt: String,

    val position: Int,
) {
    companion object {
        const val TABLE: String = "github_repo_cache"
    }
}

