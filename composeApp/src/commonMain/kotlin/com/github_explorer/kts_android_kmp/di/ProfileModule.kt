package com.github_explorer.kts_android_kmp.di

import com.github_explorer.kts_android_kmp.feature.profile.data.repo.ProfileRepositoryImpl
import com.github_explorer.kts_android_kmp.feature.profile.domain.ProfileRepository
import com.github_explorer.kts_android_kmp.feature.profile.domain.useCase.LoadUseCase
import com.github_explorer.kts_android_kmp.feature.profile.domain.useCase.LogoutUseCase
import com.github_explorer.kts_android_kmp.feature.profile.presentation.ProfileViewModel
import com.github_explorer.kts_android_kmp.core.data.storage.domain.SessionRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    factory<ProfileRepository> { ProfileRepositoryImpl(api = get()) }

    factory { LogoutUseCase(appDataCleaner = get()) }
    factory { LoadUseCase(profileRepository = get()) }

    viewModel {
        ProfileViewModel(
            loadUseCase = get(),
            logoutUseCase = get(),
            sessionRepository = get<SessionRepository>(),
        )
    }
}


