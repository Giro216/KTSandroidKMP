package com.example.kts_android_kmp.di

import com.example.kts_android_kmp.feature.mainScreen.data.repo.GitHubRepositoryImpl
import com.example.kts_android_kmp.feature.mainScreen.domain.IGitHubRepository
import com.example.kts_android_kmp.feature.mainScreen.domain.IMainUiMapper
import com.example.kts_android_kmp.feature.mainScreen.domain.usecase.SearchReposPageUseCase
import com.example.kts_android_kmp.feature.mainScreen.presentation.MainUiMapper
import com.example.kts_android_kmp.feature.mainScreen.presentation.MainViewModel
import com.example.kts_android_kmp.feature.mainScreen.presentation.reducer.MainReducer
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainScreenModule = module {
    single<IMainUiMapper> { MainUiMapper() }
    single<IGitHubRepository> { GitHubRepositoryImpl(api = get(), cache = get()) }
    viewModel { MainViewModel(searchReposPageUseCase = get(), uiMapper = get(), reducer = get()) }

    factory { SearchReposPageUseCase(repo = get()) }
    factory { MainReducer(uiMapper = get()) }

}
