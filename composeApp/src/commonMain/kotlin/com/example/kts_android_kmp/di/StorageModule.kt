package com.example.kts_android_kmp.di

import com.example.kts_android_kmp.core.data.storage.data.repo.SessionRepositoryImpl
import com.example.kts_android_kmp.core.data.storage.domain.IAppPreferences
import com.example.kts_android_kmp.core.data.storage.domain.ISessionRepository
import com.example.kts_android_kmp.core.data.storage.keyvalue.PlatformPreferences
import org.koin.dsl.module

val storageModule = module {
    single<IAppPreferences> { PlatformPreferences() }
    single<ISessionRepository> { SessionRepositoryImpl(prefs = get()) }
}