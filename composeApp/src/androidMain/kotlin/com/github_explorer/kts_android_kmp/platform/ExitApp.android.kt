package com.github_explorer.kts_android_kmp.platform

import android.app.Activity
import java.lang.ref.WeakReference

private var activityRef: WeakReference<Activity>? = null

fun setActivity(activity: Activity) {
    activityRef = WeakReference(activity)
}

actual fun exitApp() {
    activityRef?.get()?.finish()
}


