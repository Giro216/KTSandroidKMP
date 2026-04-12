package com.github_explorer.kts_android_kmp.core.config.markwon

import android.content.Context
import androidx.core.graphics.toColorInt
import com.github_explorer.kts_android_kmp.common.theme.MarkdownColors.LinkBlue
import com.github_explorer.kts_android_kmp.common.theme.MarkdownColors.LiteGrey
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.image.coil.CoilImagesPlugin

fun createMarkwon(context: Context): Markwon {
    return Markwon.builder(context)
        .usePlugin(
            CoilImagesPlugin.create(context)
        )
        .usePlugin(object : AbstractMarkwonPlugin() {
            override fun configureTheme(builder: MarkwonTheme.Builder) {
                builder
                    .linkColor(LinkBlue.toColorInt())
                    .blockQuoteColor(LiteGrey.toColorInt())
            }
        })
        .build()
}