package com.github_explorer.kts_android_kmp.di

import com.github_explorer.kts_android_kmp.core.data.storage.data.repo.SessionRepositoryImpl
import com.github_explorer.kts_android_kmp.core.data.storage.domain.AppPreferences
import com.github_explorer.kts_android_kmp.core.data.storage.domain.SessionRepository
import com.github_explorer.kts_android_kmp.core.data.storage.keyvalue.PlatformPreferencesImpl
import org.koin.dsl.module

val storageModule = module {
    single<AppPreferences> { PlatformPreferencesImpl() }
    single<SessionRepository> { SessionRepositoryImpl(prefs = get()) }
}