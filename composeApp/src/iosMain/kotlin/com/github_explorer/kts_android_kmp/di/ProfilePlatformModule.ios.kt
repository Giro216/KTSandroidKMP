package com.github_explorer.kts_android_kmp.di

import com.github_explorer.kts_android_kmp.feature.profile.platform.AppDataCleaner
import com.github_explorer.kts_android_kmp.feature.profile.platform.NoOpAppDataCleanerImpl
import org.koin.dsl.module

val profilePlatformModule = module {
    single<AppDataCleaner> { NoOpAppDataCleanerImpl() }
}

