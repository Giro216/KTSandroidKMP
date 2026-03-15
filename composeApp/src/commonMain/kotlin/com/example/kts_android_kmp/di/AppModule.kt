package com.example.kts_android_kmp.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module

val appModules = listOf(
    networkModule,
    loginScreenModule,
    mainScreenModule,

)

fun initKoin(platformSpecific: Module) {
    startKoin {
        modules(appModules + platformSpecific)
    }
}
