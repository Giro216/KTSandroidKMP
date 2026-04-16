package com.github_explorer.kts_android_kmp.feature.mainScreen.domain

import com.github_explorer.kts_android_kmp.feature.mainScreen.presentation.HintContent
import com.github_explorer.kts_android_kmp.feature.mainScreen.presentation.MainUiEvent

interface MainUiMapper {
    fun calculateHint(
        query: String,
        reposSize: Int,
        totalCount: Int,
    ): HintContent?

    fun toSearchQuery(event: MainUiEvent, currentQuery: String): String?

}