package com.github_explorer.kts_android_kmp.feature.login.oauth.platform

import com.github_explorer.kts_android_kmp.feature.login.oauth.data.network.TokensModelDto

actual class AppAuthHandler {
    actual suspend fun performTokenRequest(): TokensModelDto? {
        return null
    }
}