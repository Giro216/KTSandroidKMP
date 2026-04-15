package com.github_explorer.kts_android_kmp.core.config.markwon

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.graphics.toColorInt
import com.github_explorer.kts_android_kmp.common.theme.MarkdownColors
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
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

            override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
                builder.linkResolver { view, link ->
                    val uri = runCatching { Uri.parse(link) }.getOrNull() ?: return@linkResolver
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    val targetContext = view.context

                    if (targetContext !is Activity) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }

                    try {
                        targetContext.startActivity(intent)
                    } catch (_: ActivityNotFoundException) {
                        // Ignore: no handler for this link on device.
                    } catch (_: SecurityException) {
                        // Ignore: blocked by platform/app policy.
                    }
                }
            }
        })
        .build()
}