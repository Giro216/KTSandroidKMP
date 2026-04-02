package com.github_explorer.kts_android_kmp.di

import com.github_explorer.kts_android_kmp.feature.mainScreen.data.repo.GitHubRepositoryImpl
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.GitHubRepository
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.MainUiMapper
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.usecase.SearchReposPageUseCase
import com.github_explorer.kts_android_kmp.feature.mainScreen.presentation.MainUiMapperImpl
import com.github_explorer.kts_android_kmp.feature.mainScreen.presentation.MainViewModel
import com.github_explorer.kts_android_kmp.feature.mainScreen.presentation.reducer.MainReducer
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainScreenModule = module {
    single<MainUiMapper> { MainUiMapperImpl() }
    single<GitHubRepository> { GitHubRepositoryImpl(api = get(), cache = get()) }
    viewModel { MainViewModel(searchReposPageUseCase = get(), uiMapper = get(), reducer = get()) }

    factory { SearchReposPageUseCase(repo = get()) }
    factory { MainReducer(uiMapper = get()) }

}
