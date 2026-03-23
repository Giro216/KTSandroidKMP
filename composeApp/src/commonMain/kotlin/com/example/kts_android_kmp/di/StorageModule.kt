package com.example.kts_android_kmp.di

import com.example.kts_android_kmp.storage.data.repo.SessionRepositoryImpl
import com.example.kts_android_kmp.storage.domain.IAppPreferences
import com.example.kts_android_kmp.storage.domain.ISessionRepository
import com.example.kts_android_kmp.storage.keyvalue.PlatformPreferences
import org.koin.dsl.module

val storageModule = module {
    single<IAppPreferences> { PlatformPreferences() }
    single<ISessionRepository> { SessionRepositoryImpl(prefs = get()) }
}