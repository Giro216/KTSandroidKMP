package com.example.kts_android_kmp.core.data.network

import com.example.kts_android_kmp.feature.mainScreen.data.network.GithubRepoSearchResponseDto
import com.example.kts_android_kmp.feature.profile.data.network.GithubUserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface GitHubApi {
    suspend fun loadRepos(param: GitHubApiImpl.LoadReposRequestParam): GithubRepoSearchResponseDto

    suspend fun getCurrentUser(): GithubUserDto
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
