package com.example.kts_android_kmp.db.entity

import androidx.room.Entity

@Entity(
    tableName = GitHubRepoEntity.TABLE,
    primaryKeys = ["repoId"],
)
data class GitHubRepoEntity(
    val repoId: Long,

    val owner: String,
    val name: String,
    val description: String?,
    val language: String?,
    val stars: Int,
    val forks: Int,
    val updatedAt: String,
) {
    companion object {
        const val TABLE: String = "github_repo"
    }
}

