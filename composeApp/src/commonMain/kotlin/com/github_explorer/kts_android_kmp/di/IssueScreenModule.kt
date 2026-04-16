package com.github_explorer.kts_android_kmp.di

import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.data.repo.GithubIssueRepositoryImpl
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.GithubIssueRepository
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.useCase.CheckCreateIssuePermissionUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.useCase.CreateIssueUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.useCase.LoadIssuesUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.presentation.IssueViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val IssueScreenModule = module {
    factory<GithubIssueRepository> { GithubIssueRepositoryImpl(api = get()) }
    factory { LoadIssuesUseCase(issueRepository = get()) }
    factory { CheckCreateIssuePermissionUseCase(issueRepository = get()) }
    factory { CreateIssueUseCase(issueRepository = get()) }

    viewModel {
        IssueViewModel(
            loadIssuesUseCase = get(),
            checkCreateIssuePermissionUseCase = get(),
            createIssueUseCase = get(),
        )
    }
}

