package com.example.kts_android_kmp.di

import com.example.kts_android_kmp.network.ApiClient
import com.example.kts_android_kmp.network.GitHubApi
import com.example.kts_android_kmp.network.IGitHubApi
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> { ApiClient.httpClient }
    single<IGitHubApi> { GitHubApi(client = get()) }

}
