package com.example.kts_android_kmp.di

import com.example.kts_android_kmp.feature.profile.data.repo.ProfileRepositoryImpl
import com.example.kts_android_kmp.feature.profile.domain.IProfileRepository
import com.example.kts_android_kmp.feature.profile.domain.useCase.LoadUseCase
import com.example.kts_android_kmp.feature.profile.domain.useCase.LogoutUseCase
import com.example.kts_android_kmp.feature.profile.presentation.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    single<IProfileRepository> { ProfileRepositoryImpl(api = get()) }

    single { LogoutUseCase(appDataCleaner = get()) }
    single { LoadUseCase(profileRepository = get()) }

    viewModel {
        ProfileViewModel(
            loadUseCase = get(),
            logoutUseCase = get(),
        )
    }
}


