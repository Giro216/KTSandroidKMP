package com.example.kts_android_kmp.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val appModules = listOf(
    bootstrapModule,
    networkModule,
    storageModule,
    loginScreenModule,
    mainScreenModule,
    profileModule,

    )

fun initKoin(platformSpecific: Module = module {}) {
    startKoin {
        modules(appModules + platformSpecific)
    }
}
