package com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.platform

import android.graphics.Color
import android.widget.TextView
import androidx.core.graphics.toColorInt
import com.github_explorer.kts_android_kmp.common.theme.MarkdownColors.LiteBlack
import io.noties.markwon.Markwon

actual class MarkdownParser(
    private val markwon: Markwon
) {
    actual fun render(markdown: String, target: Any) {
        val textView = target as TextView

        textView.setTextColor(LiteBlack.toColorInt())
        textView.highlightColor = Color.TRANSPARENT

        markwon.setMarkdown(textView, markdown)
    }
}