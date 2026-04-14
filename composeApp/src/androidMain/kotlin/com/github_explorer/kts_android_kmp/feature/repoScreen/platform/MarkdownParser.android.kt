package com.github_explorer.kts_android_kmp.feature.repoScreen.platform

import android.graphics.Color
import android.widget.TextView
import com.github_explorer.kts_android_kmp.common.theme.MarkdownColors.LiteBlack
import io.noties.markwon.Markwon

actual class MarkdownParser(
    private val markwon: Markwon
) {
    actual fun render(markdown: String, target: Any) {
        val textView = target as TextView

        textView.setTextColor(Color.parseColor(LiteBlack))
        textView.highlightColor = Color.TRANSPARENT

        markwon.setMarkdown(textView, markdown)
    }
}