package com.github_explorer.kts_android_kmp.di

import com.github_explorer.kts_android_kmp.core.data.network.ApiClient
import com.github_explorer.kts_android_kmp.core.data.network.AuthTokenProvider
import com.github_explorer.kts_android_kmp.core.data.network.GitHubApi
import com.github_explorer.kts_android_kmp.core.data.network.GitHubApiImpl
import com.github_explorer.kts_android_kmp.core.data.storage.domain.SessionRepository
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {
    single { AuthTokenProvider(sessionRepository = get<SessionRepository>()) }
    single<HttpClient> { ApiClient.createHttpClient(tokenProvider = get()) }
    single<GitHubApi> { GitHubApiImpl(client = get()) }

}
