package com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.platform

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import androidx.core.graphics.toColorInt
import com.github_explorer.kts_android_kmp.common.theme.MarkdownColors.LiteBlack
import com.github_explorer.kts_android_kmp.common.theme.MarkdownColors.LiteBlackDark
import com.github_explorer.kts_android_kmp.core.config.markwon.createMarkwon

actual class MarkdownParser(
    context: Context
) {
    private val lightMarkwon = createMarkwon(context = context, isDarkTheme = false)
    private val darkMarkwon = createMarkwon(context = context, isDarkTheme = true)

    actual fun render(markdown: String, target: Any, isDarkTheme: Boolean) {
        val textView = target as TextView

        textView.setTextColor(if (!isDarkTheme) LiteBlack.toColorInt() else LiteBlackDark.toColorInt())
        textView.highlightColor = Color.TRANSPARENT

        val markwon = if (isDarkTheme) darkMarkwon else lightMarkwon
        markwon.setMarkdown(textView, markdown)
    }
}