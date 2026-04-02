package com.github_explorer.kts_android_kmp.di

import com.github_explorer.kts_android_kmp.feature.login.oauth.data.repo.AuthRepositoryImpl
import com.github_explorer.kts_android_kmp.feature.login.oauth.domain.AuthRepository
import com.github_explorer.kts_android_kmp.feature.login.oauth.presentation.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val loginViewModelModule = module {
    viewModel {
        LoginViewModel(
            authRepository = get(),
            appAuthHandler = get(),
            sessionRepository = get()
        )
    }
}

val loginAuthModule = module {
    single<AuthRepository> { AuthRepositoryImpl(sessionRepository = get()) }
}

val loginScreenModule = module {
    includes(loginViewModelModule, loginAuthModule)
}
