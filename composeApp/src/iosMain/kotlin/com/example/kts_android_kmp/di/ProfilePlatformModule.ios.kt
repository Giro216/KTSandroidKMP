package com.example.kts_android_kmp.di

import com.example.kts_android_kmp.feature.profile.platform.IAppDataCleaner
import com.example.kts_android_kmp.feature.profile.platform.NoOpAppDataCleaner
import org.koin.dsl.module

val profilePlatformModule = module {
    single<IAppDataCleaner> { NoOpAppDataCleaner() }
}

