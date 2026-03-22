package com.example.kts_android_kmp.di

import com.example.kts_android_kmp.network.ApiClient
import com.example.kts_android_kmp.network.AuthTokenProvider
import com.example.kts_android_kmp.network.GitHubApi
import com.example.kts_android_kmp.network.IGitHubApi
import com.example.kts_android_kmp.storage.domain.ISessionRepository
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {
    single { AuthTokenProvider(sessionRepository = get<ISessionRepository>()) }
    single<HttpClient> { ApiClient.createHttpClient(tokenProvider = get()) }
    single<IGitHubApi> { GitHubApi(client = get()) }

}
