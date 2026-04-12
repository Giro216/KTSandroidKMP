package com.github_explorer.kts_android_kmp.di

import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.data.repo.GithubIssueRepositoryImpl
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.GithubIssueRepository
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.presentation.IssueViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val IssueScreenModule = module {
    single<GithubIssueRepository> { GithubIssueRepositoryImpl(api = get()) }

    viewModel { IssueViewModel(repo = get()) }
}

