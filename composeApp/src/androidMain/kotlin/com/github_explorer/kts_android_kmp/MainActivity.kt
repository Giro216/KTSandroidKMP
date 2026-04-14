package com.github_explorer.kts_android_kmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github_explorer.kts_android_kmp.app.App
import com.github_explorer.kts_android_kmp.core.config.logging.initLogger
import com.github_explorer.kts_android_kmp.di.authModule
import com.github_explorer.kts_android_kmp.di.initKoin
import com.github_explorer.kts_android_kmp.di.markwonModule
import com.github_explorer.kts_android_kmp.di.roomModule
import com.github_explorer.kts_android_kmp.feature.login.oauth.platform.AppAuthHandler
import com.github_explorer.kts_android_kmp.platform.setActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val appAuthHandler by lazy { AppAuthHandler(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setActivity(this)
        appAuthHandler.init()
        initLogger()
        firebaseAnalytics = Firebase.analytics

        initKoin(
            module {
                includes(
                    authModule(appAuthHandler, this@MainActivity),
                    roomModule(this@MainActivity),
                    markwonModule()
                )
            }
        )

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
