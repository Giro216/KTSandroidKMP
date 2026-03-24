package com.example.kts_android_kmp.di

import com.example.kts_android_kmp.core.data.storage.data.repo.SessionRepositoryImpl
import com.example.kts_android_kmp.core.data.storage.domain.AppPreferences
import com.example.kts_android_kmp.core.data.storage.domain.SessionRepository
import com.example.kts_android_kmp.core.data.storage.keyvalue.PlatformPreferencesImpl
import org.koin.dsl.module

val storageModule = module {
    single<AppPreferences> { PlatformPreferencesImpl() }
    single<SessionRepository> { SessionRepositoryImpl(prefs = get()) }
}