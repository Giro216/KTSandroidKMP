package com.github_explorer.kts_android_kmp.di

import com.github_explorer.kts_android_kmp.core.data.network.ApiClient
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.data.repo.RepoFilesRepositoryImpl
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoFilesRepository
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.useCase.CreateRepoFileUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.useCase.LoadRepoContentsUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.useCase.LoadRepoFileContentUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.useCase.UpdateRepoFileUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.presentation.RepoFilesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val RepoFilesModule = module {
    single { ApiClient.json }
    single<RepoFilesRepository> { RepoFilesRepositoryImpl(api = get(), json = get()) }

    factory { LoadRepoContentsUseCase(repoFilesRepository = get()) }
    factory { LoadRepoFileContentUseCase(repoFilesRepository = get()) }
    factory { CreateRepoFileUseCase(repoFilesRepository = get()) }
    factory { UpdateRepoFileUseCase(repoFilesRepository = get()) }

    viewModel<RepoFilesViewModel> {
        RepoFilesViewModel(
            loadRepoContentsUseCase = get(),
            createRepoFileUseCase = get(),
            loadRepoFileContentUseCase = get(),
            updateRepoFileUseCase = get(),
        )
    }
}


