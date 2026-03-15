package com.example.kts_android_kmp.di

import com.example.kts_android_kmp.feature.login.oauth.AuthRepository
import com.example.kts_android_kmp.feature.login.oauth.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val loginViewModelModule = module {
    viewModel { LoginViewModel(authRepository = get(), appAuthHandler = get() ) }
}

val loginAuthModule = module {
    single { AuthRepository() }
}

val loginScreenModule = module {
    includes(loginViewModelModule, loginAuthModule)
}
