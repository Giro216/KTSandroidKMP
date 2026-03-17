package com.example.kts_android_kmp.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module

val appModules = listOf(
    bootstrapModule,
    networkModule,
    cacheModule,
    loginScreenModule,
    mainScreenModule,
    profileModule,

)

fun initKoin(platformSpecific: Module) {
    startKoin {
        modules(appModules + platformSpecific)
    }
}
