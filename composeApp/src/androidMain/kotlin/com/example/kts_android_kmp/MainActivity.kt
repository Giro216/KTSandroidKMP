package com.example.kts_android_kmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kts_android_kmp.app.App
import com.example.kts_android_kmp.di.initKoin
import com.example.kts_android_kmp.di.storageModule
import com.example.kts_android_kmp.feature.login.oauth.platform.AppAuthHandler
import com.example.kts_android_kmp.platform.setActivity
import com.example.kts_android_kmp.storage.platform.initContext

class MainActivity : ComponentActivity() {
    private val appAuthHandler by lazy { AppAuthHandler(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setActivity(this)
        appAuthHandler.init()

        initKoin(storageModule(appAuthHandler, this))
        initContext(this)

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