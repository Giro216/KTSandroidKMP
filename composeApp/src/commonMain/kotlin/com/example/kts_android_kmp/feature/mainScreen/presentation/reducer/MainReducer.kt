package com.example.kts_android_kmp.feature.mainScreen.presentation.reducer

import com.example.kts_android_kmp.feature.mainScreen.domain.IMainUiMapper
import com.example.kts_android_kmp.feature.mainScreen.presentation.HintContent
import com.example.kts_android_kmp.feature.mainScreen.presentation.MainUiState

class MainReducer(
    private val uiMapper: IMainUiMapper,
) {

    fun reduce(state: MainUiState, action: MainAction): MainUiState {
        return when (action) {
            is MainAction.FirstPageLoading -> reduceFirstPageLoading(state, action)
            is MainAction.FirstPageSuccess -> reduceFirstPageSuccess(state, action)
            is MainAction.FirstPageError -> reduceFirstPageError(state, action)
            is MainAction.NextPageLoading -> reduceNextPageLoading(state)
            is MainAction.NextPageSuccess -> reduceNextPageSuccess(state, action)
            is MainAction.NextPageError -> reduceNextPageError(state)
        }
    }

    private fun reduceFirstPageLoading(
        state: MainUiState,
        action: MainAction.FirstPageLoading
    ): MainUiState {
        return state.copy(
            query = action.query,
            isLoading = true,
            isInitialError = false,
            repos = emptyList(),
            totalCount = null,
            hint = HintContent.PlainText(""),
            pagination = MainUiState.PaginationUiState(
                page = 1,
                canPaginate = true,
                isPaginationLoading = false,
                isPaginationError = false,
            ),
        )
    }

    private fun reduceFirstPageSuccess(
        state: MainUiState,
        action: MainAction.FirstPageSuccess
    ): MainUiState {
        val repos = action.result.items
        val total = action.result.totalCount

        return state.copy(
            query = action.query,
            repos = repos,
            totalCount = total,
            isLoading = false,
            isInitialError = false,
            pagination = state.pagination.copy(
                page = 1,
                isPaginationError = false,
                isPaginationLoading = false,
                canPaginate = repos.isNotEmpty() && repos.size < total,
            ),
            hint = uiMapper.calculateHint(
                query = action.query,
                reposSize = repos.size,
                totalCount = total,
            ),
        )
    }

    private fun reduceFirstPageError(
        state: MainUiState,
        action: MainAction.FirstPageError
    ): MainUiState {
        return state.copy(
            query = action.query,
            isLoading = false,
            isInitialError = true,
            totalCount = null,
        )
    }

    private fun reduceNextPageLoading(state: MainUiState): MainUiState {
        return state.copy(
            pagination = state.pagination.copy(
                isPaginationLoading = true,
                isPaginationError = false,
            ),
        )
    }

    private fun reduceNextPageSuccess(
        state: MainUiState,
        action: MainAction.NextPageSuccess
    ): MainUiState {
        val newRepos = action.result.items
        val merged = state.repos + newRepos
        val total = state.totalCount ?: action.result.totalCount

        return state.copy(
            repos = merged,
            totalCount = total,
            pagination = state.pagination.copy(
                page = action.nextPage,
                isPaginationLoading = false,
                isPaginationError = false,
                canPaginate = newRepos.isNotEmpty() && merged.size < total,
            ),
        )
    }

    private fun reduceNextPageError(state: MainUiState): MainUiState {
        return state.copy(
            pagination = state.pagination.copy(
                isPaginationLoading = false,
                isPaginationError = true,
            ),
        )
    }
}
