package com.example.kts_android_kmp.feature.login.oauth

import androidx.core.net.toUri
import com.example.kts_android_kmp.feature.login.oauth.model.AuthConfig
import com.example.kts_android_kmp.feature.login.oauth.model.TokensModel
import kotlinx.coroutines.suspendCancellableCoroutine
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretPost
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.TokenRequest

internal object AppAuth {

    private val serviceConfiguration = AuthorizationServiceConfiguration(
        AuthConfig.AUTH_URI.toUri(),
        AuthConfig.TOKEN_URI.toUri(),
        null,
        AuthConfig.END_SESSION_URI.toUri()
    )

    fun getAuthRequest(): AuthorizationRequest {
        val builder = AuthorizationRequest.Builder(
            serviceConfiguration,
            AuthConfig.CLIENT_ID,
            ResponseTypeValues.CODE,
            AuthConfig.CALLBACK_URL.toUri()
        )
        return builder
            .setScope(AuthConfig.SCOPE)
            .build()
    }

    fun getEndSessionRequest(): EndSessionRequest {
        return EndSessionRequest.Builder(serviceConfiguration)
            .setIdTokenHint("test_id_token")
            .setPostLogoutRedirectUri(AuthConfig.LOGOUT_CALLBACK_URL.toUri())
            .build()
    }

    fun getRefreshTokenRequest(refreshToken: String): TokenRequest {
        return TokenRequest.Builder(
            serviceConfiguration,
            AuthConfig.CLIENT_ID
        )
            .setGrantType(GrantTypeValues.REFRESH_TOKEN)
            .setScopes(AuthConfig.SCOPE)
            .setRefreshToken(refreshToken)
            .build()
    }

    suspend fun performTokenRequest(
        authService: net.openid.appauth.AuthorizationService,
        tokenRequest: TokenRequest,
    ): TokensModel {
        return suspendCancellableCoroutine { continuation ->
            authService.performTokenRequest(tokenRequest, clientAuthentication) { response, ex ->
                when {
                    response != null -> {
                        val tokens = TokensModel(
                            accessToken = response.accessToken.orEmpty(),
                            refreshToken = response.refreshToken.orEmpty(),
                            idToken = response.idToken.orEmpty()
                        )
                        continuation.resumeWith(Result.success(tokens))
                    }
                    ex != null -> {
                        continuation.resumeWith(Result.failure(ex))
                    }
                    else -> error("unreachable")
                }
            }
        }
    }

    val clientAuthentication: ClientAuthentication
        get() = ClientSecretPost(AuthConfig.CLIENT_SECRET)
}



