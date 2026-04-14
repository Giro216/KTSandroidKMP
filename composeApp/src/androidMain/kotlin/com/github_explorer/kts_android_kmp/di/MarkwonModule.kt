package com.github_explorer.kts_android_kmp.di

import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.platform.MarkdownParser
import org.koin.dsl.module

fun markwonModule() = module {
    factory { MarkdownParser(context = get()) }
}