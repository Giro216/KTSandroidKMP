package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.data.repo

import com.github_explorer.kts_android_kmp.core.data.network.GitHubApi
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.data.network.toDomain
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.GithubIssue
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.GithubIssueRepository
import com.github_explorer.kts_android_kmp.utils.coRunCatching

class GithubIssueRepositoryImpl(
    private val api: GitHubApi,
) : GithubIssueRepository {
    override suspend fun canCreateIssue(owner: String): Result<Boolean> {
        return coRunCatching {
            val currentLogin = api.getCurrentUser().login
            currentLogin.equals(owner, ignoreCase = true)
        }
    }

    override suspend fun getIssues(
        owner: String,
        repo: String
    ): Result<List<GithubIssue>> {
        return coRunCatching {
            api.getCurRepoIssues(owner, repo).toDomain()
        }
    }

    override suspend fun createIssue(
        owner: String,
        repo: String,
        title: String,
        body: String?
    ): Result<GithubIssue> {
        return coRunCatching {
            api.createNewIssue(owner, repo, title, body).toDomain()
        }
    }

}