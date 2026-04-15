package com.github_explorer.kts_android_kmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github_explorer.kts_android_kmp.app.App
import com.github_explorer.kts_android_kmp.di.authModule
import com.github_explorer.kts_android_kmp.di.markwonModule
import com.github_explorer.kts_android_kmp.di.roomModule
import com.github_explorer.kts_android_kmp.feature.login.oauth.platform.AppAuthHandler
import com.github_explorer.kts_android_kmp.platform.setActivity
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import io.github.aakira.napier.Napier
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var appAuthHandler: AppAuthHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setActivity(this)

        appAuthHandler = AppAuthHandler(this)
        appAuthHandler.init()

        firebaseAnalytics = Firebase.analytics

        if (GlobalContext.getOrNull() != null) {
            val activityModules = module {
                includes(
                    authModule(appAuthHandler, this@MainActivity),
                    roomModule(this@MainActivity),
                    markwonModule()
                )
            }
            try {
                loadKoinModules(activityModules)
            } catch (e: Exception) {
                Napier.e("Error loading Koin modules: ${e.message}", e)
            }
        }

        setContent {
            App()
        }
    }

    override fun onDestroy() {
        appAuthHandler.cleanup()
        super.onDestroy()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
