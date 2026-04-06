package com.github_explorer.kts_android_kmp.core.config.markwon

import android.content.Context
import io.noties.markwon.Markwon

fun createMarkwon(context: Context): Markwon {
    return Markwon.create(context)
}