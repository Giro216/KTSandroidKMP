package com.github_explorer.kts_android_kmp.di

import com.github_explorer.kts_android_kmp.feature.repoScreen.data.repo.GithubRepoRepositoryImpl
import com.github_explorer.kts_android_kmp.feature.repoScreen.domain.GithubRepoRepository
import com.github_explorer.kts_android_kmp.feature.repoScreen.domain.useCase.LoadDetailsUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.domain.useCase.LoadReadmeUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.presentation.RepoViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val RepoScreenModule = module {
    single<GithubRepoRepository> { GithubRepoRepositoryImpl(api = get()) }

    single { LoadReadmeUseCase(repoRepository = get()) }
    single { LoadDetailsUseCase(repoRepository = get()) }

    viewModel {
        RepoViewModel(
            loadReadmeUseCase = get(),
            loadDetailsUseCase = get(),
            markdownParser = get(),
        )
    }
}