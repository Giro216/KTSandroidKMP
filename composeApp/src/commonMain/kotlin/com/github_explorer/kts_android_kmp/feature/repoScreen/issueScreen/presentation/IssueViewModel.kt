package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.presentation

import androidx.lifecycle.viewModelScope
import com.github_explorer.kts_android_kmp.common.BaseViewModel
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.useCase.CheckCreateIssuePermissionUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.useCase.CreateIssueResult
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.useCase.CreateIssueUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.useCase.CreateIssueValidationError
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.useCase.LoadIssuesUseCase
import kotlinx.coroutines.launch

class IssueViewModel(
    private val loadIssuesUseCase: LoadIssuesUseCase,
    private val checkCreateIssuePermissionUseCase: CheckCreateIssuePermissionUseCase,
    private val createIssueUseCase: CreateIssueUseCase,
) : BaseViewModel<GithubIssueUiEvent, GithubIssueUiState>(GithubIssueUiState()) {

    fun onEvent(event: GithubIssueUiEvent) {
        when (event) {
            is GithubIssueUiEvent.Init -> init(event.owner, event.repo)
            GithubIssueUiEvent.RefreshLoadIssues -> refresh()
            GithubIssueUiEvent.OpenCreateIssueDialog -> openCreateDialog()
            GithubIssueUiEvent.DismissCreateIssueDialog -> dismissCreateDialog()
            GithubIssueUiEvent.DismissOwnershipWarning -> dismissOwnershipWarning()
            GithubIssueUiEvent.DismissCreateIssueSuccess -> dismissCreateIssueSuccess()
            is GithubIssueUiEvent.IssueTitleChanged -> onTitleChanged(event.value)
            is GithubIssueUiEvent.IssueBodyChanged -> onBodyChanged(event.value)
            GithubIssueUiEvent.CreateIssue -> createIssue()
        }
    }

    private fun init(owner: String, repoName: String) {
        updateState {
            copy(
                owner = owner,
                repo = repoName,
                isOwnershipWarning = false,
                createIssueSuccessMessage = null,
            )
        }
        resolveCreatePermission(owner)
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

            val result = loadIssuesUseCase.loadIssues(owner = owner, repo = repoName)

            result
                .onSuccess { issues ->
                    updateState {
                        copy(
                            isLoading = false,
                            isError = false,
                            errorMessage = null,
                            issueList = issues,
                        )
                    }
                }
                .onFailure { throwable ->
                    updateState {
                        copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = throwable.message?.let(IssueMessage::Dynamic),
                        )
                    }
                }
        }
    }

    private fun openCreateDialog() {
        if (!state.value.canCreateIssue) {
            updateState {
                copy(
                    isOwnershipWarning = true,
                )
            }
        }

        updateState {
            copy(
                isCreateDialogOpen = true,
                createIssueError = null,
                createIssueSuccessMessage = null,
            )
        }
    }

    private fun dismissOwnershipWarning() {
        updateState { copy(isOwnershipWarning = false) }
    }

    private fun dismissCreateIssueSuccess() {
        updateState { copy(createIssueSuccessMessage = null) }
    }

    private fun resolveCreatePermission(owner: String) {
        viewModelScope.launch {
            checkCreateIssuePermissionUseCase.canCreateIssue(owner)
                .onSuccess { canCreate ->
                    updateState {
                        copy(
                            canCreateIssue = canCreate,
                            isOwnershipWarning = !canCreate,
                        )
                    }
                }
                .onFailure {
                    updateState {
                        copy(
                            canCreateIssue = false,
                            isError = true,
                            errorMessage = IssueMessage.Resource(Strings.checkRoolsErr),
                        )
                    }
                }
        }
    }

    private fun dismissCreateDialog() {
        updateState {
            copy(
                isCreateDialogOpen = false,
                issueTitleInput = "",
                issueBodyInput = "",
                isCreatingIssue = false,
                createIssueError = null,
            )
        }
    }

    private fun onTitleChanged(value: String) {
        updateState {
            copy(
                issueTitleInput = value,
                createIssueError = null,
            )
        }
    }

    private fun onBodyChanged(value: String) {
        updateState {
            copy(
                issueBodyInput = value,
                createIssueError = null,
            )
        }
    }

    private fun createIssue() {
        if (!state.value.canCreateIssue) {
            updateState {
                copy(isOwnershipWarning = true)
            }
            return
        }

        val owner = state.value.owner ?: return
        val repoName = state.value.repo ?: return
        val title = state.value.issueTitleInput
        val body = state.value.issueBodyInput

        viewModelScope.launch {
            updateState {
                copy(
                    isCreatingIssue = true,
                    createIssueError = null,
                    createIssueSuccessMessage = null,
                )
            }

            when (
                val result = createIssueUseCase.createIssue(
                    owner = owner,
                    repo = repoName,
                    title = title,
                    body = body,
                    canCreateIssue = state.value.canCreateIssue,
                )
            ) {
                is CreateIssueResult.Success -> {
                    updateState {
                        copy(
                            isCreateDialogOpen = false,
                            issueTitleInput = "",
                            issueBodyInput = "",
                            isCreatingIssue = false,
                            createIssueError = null,
                            createIssueSuccessMessage = IssueMessage.Resource(Strings.createIssueSuccessMessage),
                        )
                    }
                    loadIssues(owner, repoName)
                }

                is CreateIssueResult.ValidationError -> {
                    updateState {
                        copy(
                            isCreatingIssue = false,
                            createIssueError = when (result.reason) {
                                CreateIssueValidationError.EmptyTitle -> IssueMessage.Resource(
                                    Strings.issueNameIsRequired
                                )

                                CreateIssueValidationError.NoPermission -> IssueMessage.Resource(
                                    Strings.createIssueWarning
                                )
                            },
                        )
                    }
                }

                is CreateIssueResult.Failure -> {
                    updateState {
                        copy(
                            isCreatingIssue = false,
                            createIssueError = result.throwable.message?.let(IssueMessage::Dynamic)
                                ?: IssueMessage.Resource(Strings.createIssueError),
                        )
                    }
                }
            }
        }
    }
}
