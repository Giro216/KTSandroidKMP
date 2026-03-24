package com.example.kts_android_kmp.feature.login.oauth.platform

import android.content.Context
import com.example.kts_android_kmp.feature.login.oauth.AppAuth
import com.example.kts_android_kmp.feature.login.oauth.data.network.TokensModelDto
import io.github.aakira.napier.Napier
import net.openid.appauth.AuthorizationService

actual class TokenRefresher(private val context: Context) {
    actual suspend fun refreshToken(refreshToken: String): TokensModelDto? {
        return try {
            val authService = AuthorizationService(context)
            val tokenRequest = AppAuth.getRefreshTokenRequest(refreshToken)
            AppAuth.performTokenRequest(authService, tokenRequest)
        } catch (e: Exception) {
            Napier.e("Failed to refresh token: ${e.message}", e)
            null
        }
    }
}

