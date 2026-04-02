import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.stability.analyzer)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    id("io.gitlab.arturbosch.detekt")
//    id("com.android.application")
    id("com.google.gms.google-services")
    alias(libs.plugins.google.firebase.crashlytics)
}

repositories {
    google()
    mavenCentral()
}

tasks.matching { it.name in setOf("debugStabilityCheck", "releaseStabilityCheck") }.configureEach {
    dependsOn("compileDebugUnitTestKotlinAndroid")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.swiperefreshlayout)
            implementation(libs.coil.network.okhttp)
            implementation(libs.coil.gif)
            implementation(libs.appauth)

            implementation(libs.ktor.client.okhttp)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation("com.google.firebase:firebase-analytics")

        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.material.icons.extended)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.navigation.compose)
            implementation(libs.coil.compose)
            implementation(libs.napier)

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            // Koin DI
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Ktor core + plugins (multiplatform)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)

            implementation(libs.kotlinx.serialization.json)

            implementation(libs.androidx.datastore.preferences)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.github_explorer.kts_android_kmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    buildFeatures.buildConfig = true

    signingConfigs {
        create("release") {
            storeFile = file("C:/Users/maks0/source/Kotlin_Projects/KTS_keystore/local-keystore")
            storePassword = releaseStorePassword
            keyPassword = releaseKeyPassword
            keyAlias = releaseKeyAlias
        }
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("Boolean", "LOGGING_ENABLED", "true")
        }
        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true

            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )

            buildConfigField("Boolean", "LOGGING_ENABLED", "false")
        }
    }

    defaultConfig {
        manifestPlaceholders += mapOf()
        applicationId = "com.github_explorer.kts_android_kmp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        manifestPlaceholders["appAuthRedirectScheme"] = "ru.kts.giro216.oauth"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:34.11.0"))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)


    debugImplementation(libs.compose.uiTooling)
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")
    listOf("kspAndroid", "kspIosArm64", "kspIosSimulatorArm64").forEach {
        add(it, libs.room.compiler)
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    ignoreFailures = true


    source.setFrom(
        "src/commonMain/kotlin",
        "src/androidMain/kotlin",
        "src/commonTest/kotlin",
        "src/androidUnitTest/kotlin"
    )
}

//dependencies {
//    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
//}

val localProps = Properties().apply {
    val f = rootProject.layout.projectDirectory.file("local.properties").asFile
    if (f.exists()) f.inputStream().use { load(it) }
}

val clientIdProvider = providers.provider { localProps.getProperty("CLIENT_ID") ?: "" }
val clientSecretProvider = providers.provider { localProps.getProperty("CLIENT_SECRET") ?: "" }

val genDir = layout.buildDirectory.dir("generated/authConfig")

val generateAuthConfig by tasks.registering {
    outputs.dir(genDir)
    doLast {
        val clientId = clientIdProvider.get()
        val clientSecret = clientSecretProvider.get()

        val pkg = "com.example.kts_android_kmp.feature.login.oauth.data.network"
        val outDir = genDir.get().asFile.resolve(pkg.replace('.', '/'))
        outDir.mkdirs()
        outDir.resolve("AuthSecrets.kt").writeText(
            """
            package $pkg

            internal object AuthSecrets {
                const val CLIENT_ID = "$clientId"
                const val CLIENT_SECRET = "$clientSecret"
            }
            """.trimIndent()
        )
    }
}

val releaseStorePassword = localProps.getProperty("RELEASE_STORE_PASSWORD") ?: ""
val releaseKeyPassword = localProps.getProperty("RELEASE_KEY_PASSWORD") ?: ""
val releaseKeyAlias = localProps.getProperty("RELEASE_KEY_ALIAS") ?: ""

kotlin {
    sourceSets {
        commonMain {
            kotlin.srcDir(genDir)
        }
    }
}

tasks.named("compileKotlinMetadata").configure { dependsOn(generateAuthConfig) }
