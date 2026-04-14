package com.github_explorer.kts_android_kmp.di

import com.github_explorer.kts_android_kmp.core.config.markwon.createMarkwon
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.platform.MarkdownParser
import io.noties.markwon.Markwon
import org.koin.dsl.module

fun markwonModule() = module {
    single<Markwon> { createMarkwon(context = get()) }

    factory { MarkdownParser(markwon = get()) }
}