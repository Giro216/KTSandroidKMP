package com.github_explorer.kts_android_kmp.core.config.markwon

import android.content.Context
import androidx.core.graphics.toColorInt
import com.github_explorer.kts_android_kmp.common.theme.MarkdownColors
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.image.coil.CoilImagesPlugin

fun createMarkwon(context: Context, isDarkTheme: Boolean): Markwon {
    val linkColor = if (isDarkTheme) MarkdownColors.LinkBlueDark else MarkdownColors.LinkBlue
    val blockQuoteColor = if (isDarkTheme) MarkdownColors.LiteGreyDark else MarkdownColors.LiteGrey

    return Markwon.builder(context)
        .usePlugin(
            CoilImagesPlugin.create(context)
        )
        .usePlugin(object : AbstractMarkwonPlugin() {
            override fun configureTheme(builder: MarkwonTheme.Builder) {
                builder
                    .linkColor(linkColor.toColorInt())
                    .blockQuoteColor(blockQuoteColor.toColorInt())
            }
        })
        .build()
}