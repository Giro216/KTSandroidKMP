package com.example.kts_android_kmp.feature.main

import androidx.lifecycle.ViewModel
import com.example.kts_android_kmp.feature.main.models.GitHubRepo
import com.example.kts_android_kmp.feature.main.models.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        MainUiState(
            allRepos = demoRepos(),
            filteredRepos = emptyList(),
            query = "",
        )
    )
    val state: StateFlow<MainUiState> = _state.asStateFlow()

    fun onQueryChanged(value: String) {
        _state.update { it.copy(query = value) }
    }

    fun onSearch() {
        val query = state.value.query.trim()
        val all = state.value.allRepos

        val filtered = if (query.isEmpty()) {
            emptyList()
        } else {
            all.filter { repo ->
                repo.name.contains(query, ignoreCase = true) ||
                    repo.owner.contains(query, ignoreCase = true) ||
                    (repo.description?.contains(query, ignoreCase = true) == true)
            }
        }

        _state.update { it.copy(filteredRepos = filtered) }
    }

    private fun demoRepos(): List<GitHubRepo> = listOf(
        GitHubRepo(
            id = 1,
            owner = "JetBrains",
            name = "kotlin",
            description = "The Kotlin Programming Language.",
            language = "Kotlin",
            stars = 50000,
            forks = 6000,
            updatedAt = "Updated 2 days ago",
        ),
        GitHubRepo(
            id = 2,
            owner = "ktorio",
            name = "ktor",
            description = "Ktor: Framework for quickly creating connected applications.",
            language = "Kotlin",
            stars = 14000,
            forks = 1200,
            updatedAt = "Updated yesterday",
        ),
        GitHubRepo(
            id = 3,
            owner = "android",
            name = "compose-samples",
            description = "Official Jetpack Compose samples.",
            language = "Kotlin",
            stars = 22000,
            forks = 5000,
            updatedAt = "Updated 5 days ago",
        ),
        GitHubRepo(
            id = 4,
            owner = "square",
            name = "okhttp",
            description = "Square’s meticulous HTTP client.",
            language = "Kotlin",
            stars = 46000,
            forks = 9200,
            updatedAt = "Updated 3 days ago",
        ),
        GitHubRepo(
            id = 5,
            owner = "coil-kt",
            name = "coil",
            description = "Image loading for Android and Compose.",
            language = "Kotlin",
            stars = 12000,
            forks = 700,
            updatedAt = "Updated 1 week ago",
        ),
        GitHubRepo(
            id = 6,
            owner = "Kotlin",
            name = "kotlinx.serialization",
            description = "Kotlin multiplatform / multi-format serialization.",
            language = "Kotlin",
            stars = 6000,
            forks = 700,
            updatedAt = "Updated 4 days ago",
        ),
        GitHubRepo(
            id = 7,
            owner = "facebook",
            name = "react",
            description = "The library for web and native user interfaces.",
            language = "JavaScript",
            stars = 235000,
            forks = 48000,
            updatedAt = "Updated today",
        ),
        GitHubRepo(
            id = 8,
            owner = "torvalds",
            name = "linux",
            description = "Linux kernel source tree.",
            language = "C",
            stars = 190000000,
            forks = 55000,
            updatedAt = "Updated today",
        ),
    )
}
