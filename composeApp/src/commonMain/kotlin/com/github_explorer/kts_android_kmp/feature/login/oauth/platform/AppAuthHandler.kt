package com.github_explorer.kts_android_kmp.feature.login.oauth.platform

import com.github_explorer.kts_android_kmp.feature.login.oauth.data.network.TokensModelDto

expect class AppAuthHandler {
    suspend fun performTokenRequest(): TokensModelDto?
}

