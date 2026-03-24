package com.example.kts_android_kmp.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = GitHubSearchResultEntity.TABLE,
    primaryKeys = ["queryId", "repoId"],
    foreignKeys = [
        ForeignKey(
            entity = GitHubSearchQueryEntity::class,
            parentColumns = ["id"],
            childColumns = ["queryId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class GitHubSearchResultEntity(
    val queryId: Long,
    val repoId: Long,
    val position: Int,
) {
    companion object {
        const val TABLE: String = "github_search_result"
    }
}


