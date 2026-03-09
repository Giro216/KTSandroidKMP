package com.example.kts_android_kmp.network

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get


class GitHubApi(
    private val client: HttpClient = ApiClient.httpClient,
) {

    suspend fun loadRepos(param: LoadReposRequestParam): List<GithubRepoDto> {
        val response: GithubRepoSearchResponseDto = client.get("https://api.github.com/search/repositories", block = {
            url {
                parameters.append("q", param.query)
                param.sort?.let { parameters.append("sort", it.toString()) }
                param.order?.let {
                    if (param.sort != null) parameters.append("order", it)
                }
                param.perPage?.let { parameters.append("per_page", it.toString()) }
                param.page?.let { parameters.append("page", it.toString()) }
            }
            Napier.i(tag = "GithubApi") { "Request URL: ${url.buildString()}" }
        }).body()

        return response.items
    }

    abstract class RequestParam

    class LoadReposRequestParam(
        val query: String,
        val sort: SortType? = null,
        val order: String? = null,
        val perPage: Int? = null,
        val page: Int? = null,
    ) : RequestParam() {


        enum class SortType(string: String) {
            STARS("stars"),
            FORKS("forks"),
            HELP_WANTED_ISSUES("help-wanted-issues"),
            UPDATED("updated");
        }
    }
}
