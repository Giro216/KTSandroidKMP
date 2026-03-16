package com.example.kts_android_kmp.di

import android.content.Context
import com.example.kts_android_kmp.feature.login.oauth.platform.AppAuthHandler
import com.example.kts_android_kmp.feature.login.oauth.platform.TokenRefresher
import com.example.kts_android_kmp.storage.data.repo.SessionRepositoryImpl
import com.example.kts_android_kmp.storage.domain.IAppPreferences
import com.example.kts_android_kmp.storage.domain.ISessionRepository
import com.example.kts_android_kmp.storage.platform.PlatformPreferences
import org.koin.dsl.module

fun storageModule(appAuthHandler: AppAuthHandler, context: Context) = module {
    single { appAuthHandler }
    single { TokenRefresher(context.applicationContext) }

    single<IAppPreferences> { PlatformPreferences() }
    single<ISessionRepository> { SessionRepositoryImpl(prefs = get()) }
}

