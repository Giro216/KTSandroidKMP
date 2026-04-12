package com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.platform

expect class MarkdownParser {
    fun render(markdown: String, target: Any)
}
