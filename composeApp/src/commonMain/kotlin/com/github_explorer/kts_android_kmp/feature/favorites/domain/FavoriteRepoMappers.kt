package com.github_explorer.kts_android_kmp.feature.favorites.domain

import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.GitHubRepo
import com.github_explorer.kts_android_kmp.feature.repoScreen.domain.GithubRepoDetails

fun GithubRepoDetails.toGitHubRepo(): GitHubRepo {
    return GitHubRepo(
        id = id,
        owner = owner,
        name = name,
        description = description,
        language = language,
        stars = starsCount,
        forks = forksCount,
        updatedAt = updatedAt,
    )
}


