package com.github_explorer.kts_android_kmp.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = GitHubSearchQueryEntity.TABLE,
    indices = [
        Index(value = ["queryNormalized"], unique = true),
    ],
)
data class GitHubSearchQueryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val queryNormalized: String,
) {
    companion object {
        const val TABLE: String = "github_search_query"
    }
}

