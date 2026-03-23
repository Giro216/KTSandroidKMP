package com.example.kts_android_kmp.di

import android.content.Context
import com.example.kts_android_kmp.feature.login.oauth.platform.AppAuthHandler
import com.example.kts_android_kmp.feature.login.oauth.platform.TokenRefresher
import org.koin.dsl.module

fun authModule(appAuthHandler: AppAuthHandler, context: Context) = module {
    single<Context> { context.applicationContext }
    single { appAuthHandler }
    single { TokenRefresher(context.applicationContext) }
}

