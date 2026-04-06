package com.github_explorer.kts_android_kmp.feature.repoScreen.platform

import android.widget.TextView
import io.noties.markwon.Markwon

actual class MarkdownParser(
    private val markwon: Markwon
) {
    actual fun render(markdown: String, target: Any) {
        val textView = target as TextView
        markwon.setMarkdown(textView, markdown)
    }
}