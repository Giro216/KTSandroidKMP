package com.example.kts_android_kmp.di

import com.example.kts_android_kmp.feature.bootstrap.BootstrapViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val bootstrapModule = module {
    viewModel { BootstrapViewModel(sessionRepository = get()) }
}