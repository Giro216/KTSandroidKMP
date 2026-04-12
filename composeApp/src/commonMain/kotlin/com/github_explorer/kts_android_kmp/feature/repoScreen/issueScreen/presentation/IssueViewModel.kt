package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.presentation

import androidx.lifecycle.viewModelScope
import com.github_explorer.kts_android_kmp.common.BaseViewModel
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.GithubIssueRepository
import kotlinx.coroutines.launch

class IssueViewModel(
    private val repo: GithubIssueRepository,
) : BaseViewModel<GithubIssueUiEvent, GithubIssueUiState>(GithubIssueUiState()) {

    fun onEvent(event: GithubIssueUiEvent) {
        when (event) {
            is GithubIssueUiEvent.Init -> init(event.owner, event.repo)
            GithubIssueUiEvent.RefreshLoadIssues -> refresh()
            GithubIssueUiEvent.CreateIssue -> Unit // пока не реализовано
        }
    }

    private fun init(owner: String, repoName: String) {
        updateState {
            copy(
                owner = owner,
                repo = repoName,
            )
        }
        loadIssues(owner, repoName)
    }

    private fun refresh() {
        val owner = state.value.owner ?: return
        val repoName = state.value.repo ?: return
        loadIssues(owner, repoName)
    }

    private fun loadIssues(owner: String, repoName: String) {
        viewModelScope.launch {
            updateState {
                copy(
                    isLoading = true,
                    isError = false,
                    errorMessage = null,
                )
            }

            val result = repo.getIssues(owner = owner, repo = repoName)

            result
                .onSuccess { issues ->
                    updateState {
                        copy(
                            isLoading = false,
                            isError = false,
                            errorMessage = null,
                            issueList = ArrayList(issues),
                        )
                    }
                }
                .onFailure { throwable ->
                    updateState {
                        copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = throwable.message,
                        )
                    }
                }
        }
    }
}
