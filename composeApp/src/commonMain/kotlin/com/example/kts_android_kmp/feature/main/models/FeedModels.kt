package com.example.kts_android_kmp.feature.main.models

import androidx.compose.runtime.Immutable

@Immutable
data class FeedPost(
    val id: String,
    val authorName: String,
    val authorSubtitle: String,
    val text: String,
    val likes: Int,
    val comments: Int,
    val reposts: Int,
    val views: Int,
    val imageUrl: String? = null,
)

@Immutable
data class MainUiState(
    val posts: List<FeedPost> = emptyList(),
    val isRefreshing: Boolean = false,
)

