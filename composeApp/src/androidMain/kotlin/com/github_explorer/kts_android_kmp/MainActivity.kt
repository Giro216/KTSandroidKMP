package com.github_explorer.kts_android_kmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github_explorer.kts_android_kmp.app.App
import com.github_explorer.kts_android_kmp.di.authModule
import com.github_explorer.kts_android_kmp.di.initKoin
import com.github_explorer.kts_android_kmp.di.roomModule
import com.github_explorer.kts_android_kmp.feature.login.oauth.platform.AppAuthHandler
import com.github_explorer.kts_android_kmp.platform.setActivity
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    private val appAuthHandler by lazy { AppAuthHandler(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setActivity(this)
        appAuthHandler.init()

        initKoin(
            module {
                includes(
                    authModule(appAuthHandler, this@MainActivity),
                    roomModule(this@MainActivity),
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