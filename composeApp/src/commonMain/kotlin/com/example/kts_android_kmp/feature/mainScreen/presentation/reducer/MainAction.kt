package com.example.kts_android_kmp.feature.mainScreen.presentation.reducer

import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubSearchResult

sealed interface MainAction {
    data class FirstPageLoading(val query: String) : MainAction
    data class FirstPageSuccess(val query: String, val result: GitHubSearchResult) : MainAction
    data class FirstPageError(val query: String, val throwable: Throwable) : MainAction

    data class NextPageLoading(val nextPage: Int) : MainAction
    data class NextPageSuccess(val nextPage: Int, val result: GitHubSearchResult) : MainAction
    data class NextPageError(val throwable: Throwable) : MainAction
}

