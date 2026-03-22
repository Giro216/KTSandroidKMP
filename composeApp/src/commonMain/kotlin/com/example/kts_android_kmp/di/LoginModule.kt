package com.example.kts_android_kmp.di

import com.example.kts_android_kmp.feature.login.oauth.data.repo.AuthRepository
import com.example.kts_android_kmp.feature.login.oauth.domain.IAuthRepository
import com.example.kts_android_kmp.feature.login.oauth.presentation.LoginViewModel
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
    single<IAuthRepository> { AuthRepository(sessionRepository = get()) }
}

val loginScreenModule = module {
    includes(loginViewModelModule, loginAuthModule)
}
