package com.example.kts_android_kmp.feature.login.oauth

import android.content.Context
import com.example.kts_android_kmp.feature.login.oauth.model.TokensModel
import net.openid.appauth.AuthorizationService

actual class TokenRefresher(private val context: Context) {
    actual suspend fun refreshToken(refreshToken: String): TokensModel? {
        return try {
            val authService = AuthorizationService(context)
            val tokenRequest = AppAuth.getRefreshTokenRequest(refreshToken)
            AppAuth.performTokenRequest(authService, tokenRequest)
        } catch (e: Exception) {
            // TODO: Log the exception
            null
        }
    }
}

