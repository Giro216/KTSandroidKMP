package com.example.kts_android_kmp.feature.profile.domain

import androidx.compose.runtime.Immutable

@Immutable
data class UserProfile(
    val avatarUrl: String,
    val name: String,
    val bio: String?,
    val publicRepos: Int,
    val followers: Int,
)
