package com.github_explorer.kts_android_kmp.feature.profile.userRepoScreen.data.repo

import com.github_explorer.kts_android_kmp.core.data.network.GitHubApi
import com.github_explorer.kts_android_kmp.feature.mainScreen.data.network.GithubRepoDto
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.GitHubRepo
import com.github_explorer.kts_android_kmp.feature.profile.userRepoScreen.domain.UserRepoRepository
import com.github_explorer.kts_android_kmp.utils.coRunCatching

private const val PER_PAGE = 100

class UserRepoRepositoryImpl(
    private val api: GitHubApi,
) : UserRepoRepository {

    override suspend fun loadUserRepos(): Result<List<GitHubRepo>> {
        return coRunCatching {
            buildList {
                var page = 1

                while (true) {
                    val repos = api.loadCurrentUserRepos(page = page, perPage = PER_PAGE)
                    addAll(repos.map { it.toDomain() })

                    if (repos.size < PER_PAGE) break
                    page++
                }
            }
        }
    }
}

private fun GithubRepoDto.toDomain(): GitHubRepo {
    return GitHubRepo(
        id = id,
        owner = owner.login,
        name = name,
        description = description,
        language = language,
        stars = stargazersCount,
        forks = forksCount,
        updatedAt = updatedAt,
    )
}

