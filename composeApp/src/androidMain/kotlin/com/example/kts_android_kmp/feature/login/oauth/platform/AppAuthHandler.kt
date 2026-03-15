package com.example.kts_android_kmp.feature.login.oauth.platform

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kts_android_kmp.feature.login.oauth.AppAuth
import com.example.kts_android_kmp.feature.login.oauth.data.network.TokensModel
import kotlinx.coroutines.suspendCancellableCoroutine
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import kotlin.coroutines.resumeWithException

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class AppAuthHandler(private val activity: ComponentActivity) {

    private val authService: AuthorizationService = AuthorizationService(activity)

    private lateinit var authResultLauncher: ActivityResultLauncher<Intent>

    private var continuation: ((Result<TokensModel>) -> Unit)? = null

    fun init() {
        authResultLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val data = result.data
            when {
                data != null -> {
                    val response = AuthorizationResponse.fromIntent(data)
                    val error = AuthorizationException.fromIntent(data)
                    when {
                        response != null -> {
                            authService.performTokenRequest(
                                response.createTokenExchangeRequest(),
                                AppAuth.clientAuthentication
                            ) { resp, ex ->
                                when {
                                    resp != null -> {
                                        val tokens = TokensModel(
                                            accessToken = resp.accessToken.orEmpty(),
                                            refreshToken = resp.refreshToken.orEmpty(),
                                            idToken = resp.idToken.orEmpty()
                                        )
                                        continuation?.invoke(Result.success(tokens))
                                    }
                                    ex != null -> continuation?.invoke(Result.failure(ex))
                                    else -> continuation?.invoke(Result.failure(IllegalStateException("unreachable")))
                                }
                            }
                        }
                        error != null -> continuation?.invoke(Result.failure(error))
                        else -> continuation?.invoke(Result.failure(IllegalStateException("unreachable")))
                    }
                }
                else -> continuation?.invoke(Result.failure(IllegalStateException("Authentication cancelled")))
            }
        }
    }

    actual suspend fun performTokenRequest(): TokensModel? = suspendCancellableCoroutine { cont ->
        continuation = { result ->
            result.onSuccess { cont.resume(it, null) }
            result.onFailure { cont.resumeWithException(it) }
        }
        val authRequest = AppAuth.getAuthRequest()
        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        authResultLauncher.launch(authIntent)
    }
}
