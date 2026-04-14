package com.github_explorer.kts_android_kmp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = FavoriteRepoEntity.TABLE,
)
data class FavoriteRepoEntity(
    @PrimaryKey val repoId: Long,

    val owner: String,
    val name: String,
    val description: String?,
    val language: String?,
    val stars: Int,
    val forks: Int,
    val updatedAt: String,

    /** unix millis */
    val savedAt: Long,
) {
    companion object {
        const val TABLE: String = "favorite_repo"
    }
}

