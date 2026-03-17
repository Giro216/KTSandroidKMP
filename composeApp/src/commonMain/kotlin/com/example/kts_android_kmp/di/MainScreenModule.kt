package com.example.kts_android_kmp.di

import com.example.kts_android_kmp.feature.mainScreen.data.mapper.MainUiMapper
import com.example.kts_android_kmp.feature.mainScreen.data.repo.GitHubRepositoryImpl
import com.example.kts_android_kmp.feature.mainScreen.domain.IGitHubRepository
import com.example.kts_android_kmp.feature.mainScreen.domain.IMainUiMapper
import com.example.kts_android_kmp.feature.mainScreen.presentation.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainScreenModule = module {
    viewModel { MainViewModel(repo = get(), uiMapper = get()) }
    single<IMainUiMapper> { MainUiMapper() }
    single<IGitHubRepository> { GitHubRepositoryImpl(api = get()) }

}
