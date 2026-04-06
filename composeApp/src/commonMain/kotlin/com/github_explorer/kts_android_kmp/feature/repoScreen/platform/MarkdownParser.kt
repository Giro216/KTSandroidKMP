package com.github_explorer.kts_android_kmp.feature.repoScreen.platform

expect class MarkdownParser {
    fun render(markdown: String, target: Any)
}
