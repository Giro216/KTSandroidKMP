package com.example.kts_android_kmp.di

import com.example.kts_android_kmp.feature.mainScreen.data.network.GitHubApi
import com.example.kts_android_kmp.feature.mainScreen.data.network.IGitHubApi
import com.example.kts_android_kmp.network.ApiClient
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> { ApiClient.httpClient }
    single<IGitHubApi> { GitHubApi(client = get()) }

}
