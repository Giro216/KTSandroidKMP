package com.example.kts_android_kmp.network

import com.example.kts_android_kmp.feature.mainScreen.models.GithubRepoSearchResponseDto
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol

interface IGitHubApi {
    suspend fun loadRepos(param: GitHubApi.LoadReposRequestParam): GithubRepoSearchResponseDto
}

class GitHubApi(
    private val client: HttpClient,
) : IGitHubApi {

    override suspend fun loadRepos(param: LoadReposRequestParam): GithubRepoSearchResponseDto {
        return client.get("/search/repositories") {
            // Почему-то без этого определения url не работает, хотя в ApiClient уже есть defaultRequest с url
            url {
                protocol = URLProtocol.HTTPS
                host = ApiConfig.GITHUB_API_HOST
            }
            parameter("q", param.query)
            param.sort?.let { parameter("sort", it.toString()) }
            param.order?.let {
                if (param.sort != null) parameter("order", it)
            }
            param.perPage?.let { parameter("per_page", it.toString()) }
            param.page?.let { parameter("page", it.toString()) }
            Napier.i(tag = "GithubApi") { "Request URL: ${url.buildString()}" }
        }.body()
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
