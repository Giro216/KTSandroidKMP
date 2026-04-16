package com.github_explorer.kts_android_kmp.di

import com.github_explorer.kts_android_kmp.core.data.storage.domain.SessionRepository
import com.github_explorer.kts_android_kmp.feature.profile.userRepoScreen.data.repo.UserRepoRepositoryImpl
import com.github_explorer.kts_android_kmp.feature.profile.userRepoScreen.domain.UserRepoRepository
import com.github_explorer.kts_android_kmp.feature.profile.userRepoScreen.domain.useCase.LoadUserRepoUseCase
import com.github_explorer.kts_android_kmp.feature.profile.userRepoScreen.presentation.UserRepoViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val userReposModule = module {
    factory<UserRepoRepository> { UserRepoRepositoryImpl(api = get()) }
    factory { LoadUserRepoUseCase(userRepoRepository = get()) }

    viewModel {
        UserRepoViewModel(
            loadUseCase = get(),
            sessionRepository = get<SessionRepository>(),
        )
    }
}
