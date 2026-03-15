package com.example.kts_android_kmp.di

import com.example.kts_android_kmp.feature.mainScreen.MainViewModel
import com.example.kts_android_kmp.feature.mainScreen.models.GitHubRepositoryImpl
import com.example.kts_android_kmp.feature.mainScreen.models.IGitHubRepository
import com.example.kts_android_kmp.feature.mainScreen.models.IMainUiMapper
import com.example.kts_android_kmp.feature.mainScreen.models.MainUiMapper
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainScreenModule = module {
    viewModel { MainViewModel(repo = get(), uiMapper = get()) }
    single<IMainUiMapper> { MainUiMapper() }
    single<IGitHubRepository> { GitHubRepositoryImpl(api = get()) }

}
