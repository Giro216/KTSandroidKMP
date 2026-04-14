package com.github_explorer.kts_android_kmp.core.data.network

import com.github_explorer.kts_android_kmp.feature.mainScreen.data.network.GithubRepoSearchResponseDto
import com.github_explorer.kts_android_kmp.feature.profile.data.network.GithubUserDto
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.data.network.CreateIssueRequestDto
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.data.network.GithubIssueDto
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.data.network.GithubRepoDetailsDto
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.data.network.GithubRepoReadmeDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

interface GitHubApi {
    suspend fun loadRepos(param: GitHubApiImpl.LoadReposRequestParam): GithubRepoSearchResponseDto

    suspend fun getCurrentUser(): GithubUserDto

    suspend fun getCurRepoReadme(owner: String, repo: String): GithubRepoReadmeDto

    suspend fun getCurRepoDetails(owner: String, repo: String): GithubRepoDetailsDto

    suspend fun getCurRepoIssues(owner: String, repo: String): List<GithubIssueDto>

    suspend fun createNewIssue(
        owner: String,
        repo: String,
        title: String,
        body: String? = null
    ): GithubIssueDto
}

class GitHubApiImpl(
    private val client: HttpClient,
) : GitHubApi {

    override suspend fun loadRepos(param: LoadReposRequestParam): GithubRepoSearchResponseDto {
        return client.get("/search/repositories") {
            parameter("q", param.query)
            param.sort?.let { parameter("sort", it.toString()) }
            param.order?.let {
                if (param.sort != null) parameter("order", it)
            }
            param.perPage?.let { parameter("per_page", it.toString()) }
            param.page?.let { parameter("page", it.toString()) }
        }.body()
    }

    override suspend fun getCurrentUser(): GithubUserDto {
        return client.get("/user") {
        }.body<GithubUserDto>()
    }

    override suspend fun getCurRepoReadme(owner: String, repo: String): GithubRepoReadmeDto {
        return client.get("/repos/$owner/$repo/readme") {
        }.body<GithubRepoReadmeDto>()
    }

    override suspend fun getCurRepoDetails(owner: String, repo: String): GithubRepoDetailsDto {
        return client.get("/repos/$owner/$repo") {
        }.body<GithubRepoDetailsDto>()
    }

    override suspend fun getCurRepoIssues(owner: String, repo: String): List<GithubIssueDto> {
        return client.get("/repos/$owner/$repo/issues") {
        }.body<List<GithubIssueDto>>()
    }

    override suspend fun createNewIssue(
        owner: String,
        repo: String,
        title: String,
        body: String?
    ): GithubIssueDto {
        return client.post(urlString = "/repos/$owner/$repo/issues") {
            setBody(CreateIssueRequestDto(title = title, body = body))
        }.body<GithubIssueDto>()
    }


    class LoadReposRequestParam(
        val query: String,
        val sort: SortType? = null,
        val order: String? = null,
        val perPage: Int? = null,
        val page: Int? = null,
    ) {

        enum class SortType(private val value: String) {
            STARS("stars"),
            FORKS("forks"),
            HELP_WANTED_ISSUES("help-wanted-issues"),
            UPDATED("updated");

            override fun toString(): String = value
        }
    }
}
