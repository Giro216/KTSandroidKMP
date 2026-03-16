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
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class AppAuthHandler(private val activity: ComponentActivity) {

    private val authService: AuthorizationService = AuthorizationService(activity)

    private lateinit var authResultLauncher: ActivityResultLauncher<Intent>

    private var continuation: ((Result<TokensModel>) -> Unit)? = null

    private fun dispatch(result: Result<TokensModel>) {
        val cb = continuation ?: return
        continuation = null
        cb(result)
    }

    private fun onAuthResult(data: Intent?) {
        if (data == null) {
            dispatch(Result.failure(IllegalStateException("Authentication cancelled")))
            return
        }

        AuthorizationException.fromIntent(data)?.let { ex ->
            dispatch(Result.failure(ex))
            return
        }

        val response = AuthorizationResponse.fromIntent(data)
        if (response == null) {
            dispatch(Result.failure(IllegalStateException("Missing authorization response")))
            return
        }

        authService.performTokenRequest(
            response.createTokenExchangeRequest(),
            AppAuth.clientAuthentication,
        ) { resp, ex ->
            when {
                ex != null -> dispatch(Result.failure(ex))
                resp != null -> dispatch(
                    Result.success(
                        TokensModel(
                            accessToken = resp.accessToken.orEmpty(),
                            refreshToken = resp.refreshToken.orEmpty(),
                            idToken = resp.idToken.orEmpty(),
                        )
                    )
                )

                else -> dispatch(Result.failure(IllegalStateException("unreachable")))
            }
        }
    }

    fun init() {
        authResultLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            onAuthResult(result.data)
        }
    }

    actual suspend fun performTokenRequest(): TokensModel? = suspendCancellableCoroutine { cont ->
        continuation = { result ->
            result.onSuccess { cont.resume(it) }
            result.onFailure { cont.resumeWithException(it) }
        }

        cont.invokeOnCancellation {
            continuation = null
        }

        val authRequest = AppAuth.getAuthRequest()
        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        authResultLauncher.launch(authIntent)
    }
}
