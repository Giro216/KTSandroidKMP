package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain

interface GithubIssueRepository {
    suspend fun getIssues(
        owner: String,
        repo: String,
    ): Result<List<GithubIssue>>

//    suspend fun createIssue(
//        owner: String,
//        repo: String,
//        title: String,
//        body: String,
//    ): Result<GithubIssue>
}