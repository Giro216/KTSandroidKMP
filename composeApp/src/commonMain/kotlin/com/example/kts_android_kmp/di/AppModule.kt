package com.example.kts_android_kmp.di

import com.example.kts_android_kmp.feature.login.oauth.AuthRepository
import com.example.kts_android_kmp.feature.login.oauth.LoginViewModel
import com.example.kts_android_kmp.feature.main.MainViewModel
import com.example.kts_android_kmp.feature.main.models.GitHubRepositoryImpl
import com.example.kts_android_kmp.feature.main.models.IGitHubRepository
import com.example.kts_android_kmp.feature.main.models.IMainUiMapper
import com.example.kts_android_kmp.feature.main.models.MainUiMapper
import com.example.kts_android_kmp.network.ApiClient
import com.example.kts_android_kmp.network.GitHubApi
import com.example.kts_android_kmp.network.IGitHubApi
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> { ApiClient.httpClient }

    single<IGitHubApi> { GitHubApi(client = get()) }
}

val repositoryModule = module {
    single<IGitHubRepository> { GitHubRepositoryImpl(api = get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(repo = get(), uiMapper = get()) }
    viewModel { LoginViewModel(authRepository = get(), appAuthHandler = get() ) }
}

val mapperModule = module {
    single<IMainUiMapper> { MainUiMapper() }
}

val authModule = module {
    single { AuthRepository() }
}

val appModules = listOf(
    networkModule,
    repositoryModule,
    viewModelModule,
    mapperModule,
    authModule,
)

fun initKoin(platformSpecific: Module) {
    startKoin {
        modules(appModules + platformSpecific)
    }
}
