package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain

import androidx.compose.runtime.Immutable

@Immutable
data class GithubIssue(
    val id: Long,
    val number: Int,
    val title: String,
    val state: State,
) {
    enum class State {
        OPEN,
        CLOSED,
        UNKNOWN,
    }
}