package com.github_explorer.kts_android_kmp.feature.profile.mainProfileScreen.domain

import androidx.compose.runtime.Immutable

@Immutable
data class UserProfile(
    val avatarUrl: String,
    val name: String,
    val bio: String?,
    val publicRepos: Int,
    val privateRepos: Int,
    val followers: Int,
)
