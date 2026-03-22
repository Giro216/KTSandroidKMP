package com.example.kts_android_kmp.feature.mainScreen.domain

import androidx.compose.ui.graphics.Color
import com.example.kts_android_kmp.feature.mainScreen.presentation.HintContent
import com.example.kts_android_kmp.feature.mainScreen.presentation.MainUiEvent

interface IMainUiMapper {
    fun calculateHint(
        query: String,
        reposSize: Int,
        totalCount: Int,
    ): HintContent?

    fun toSearchQuery(event: MainUiEvent, currentQuery: String): String?

    fun formatCount(count: Int): String

    fun formatMetric(emoji: String, count: Int): String

    fun colorForLanguage(language: String): Color
}