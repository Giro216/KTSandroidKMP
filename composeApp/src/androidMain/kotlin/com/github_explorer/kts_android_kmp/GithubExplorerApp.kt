package com.github_explorer.kts_android_kmp

import android.app.Application
import com.github_explorer.kts_android_kmp.core.config.logging.initLogger
import com.github_explorer.kts_android_kmp.di.initKoin

class GithubExplorerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initLogger()
        initKoin()

        appInstance = this
    }

    companion object {
        var appInstance: GithubExplorerApp? = null
            private set
    }
}


