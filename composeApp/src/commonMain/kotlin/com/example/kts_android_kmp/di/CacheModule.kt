package com.example.kts_android_kmp.di

import com.example.kts_android_kmp.feature.mainScreen.domain.cache.IGitHubRepoCache
import com.example.kts_android_kmp.feature.mainScreen.domain.cache.NoOpGitHubRepoCache
import org.koin.dsl.module

val cacheModule = module {
    single<IGitHubRepoCache> { NoOpGitHubRepoCache() }
}


