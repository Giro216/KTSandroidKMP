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

    applyDefaultHierarchyTemplate()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.coil2.legacy)
            implementation(libs.androidx.swiperefreshlayout)
            implementation(libs.coil.network.okhttp)
            implementation(libs.coil.gif)
            implementation(libs.appauth)

            implementation(libs.ktor.client.okhttp)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation("com.google.firebase:firebase-analytics")

            implementation(libs.markwon.core)
            implementation(libs.markwon.image.coil)
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

compose.resources {
    publicResClass = true
    packageOfResClass = "ktsandroidkmp.composeapp.generated.resources"
    generateResClass = auto
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
            resValue("string", "app_name", "Github Explorer")
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

abstract class GenerateAuthConfigTask : DefaultTask() {
    @get:Input
    abstract val clientId: Property<String>

    @get:Input
    abstract val clientSecret: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val pkg = "com.github_explorer.kts_android_kmp.feature.login.oauth.data.network"
        val outDir = outputDir.get().asFile.resolve(pkg.replace('.', '/'))
        outDir.mkdirs()
        outDir.resolve("AuthSecrets.kt").writeText(
            """
            package $pkg

            internal object AuthSecrets {
                const val CLIENT_ID = "${clientId.get()}"
                const val CLIENT_SECRET = "${clientSecret.get()}"
            }
            """.trimIndent()
        )
    }
}

val localProps = Properties().apply {
    val f = rootProject.layout.projectDirectory.file("local.properties").asFile
    if (f.exists()) f.inputStream().use { load(it) }
}

val localClientId = localProps.getProperty("CLIENT_ID").orEmpty()
val localClientSecret = localProps.getProperty("CLIENT_SECRET").orEmpty()

val clientIdProvider = providers.gradleProperty("CLIENT_ID")
    .orElse(providers.environmentVariable("CLIENT_ID"))
    .orElse(localClientId)
val clientSecretProvider = providers.gradleProperty("CLIENT_SECRET")
    .orElse(providers.environmentVariable("CLIENT_SECRET"))
    .orElse(localClientSecret)

val genDir = layout.buildDirectory.dir("generated/authConfig")

val generateAuthConfig by tasks.registering(GenerateAuthConfigTask::class) {
    clientId.set(clientIdProvider)
    clientSecret.set(clientSecretProvider)
    outputDir.set(genDir)
}

val releaseStorePassword = providers.gradleProperty("RELEASE_STORE_PASSWORD")
    .orElse(providers.environmentVariable("RELEASE_STORE_PASSWORD"))
    .orElse(localProps.getProperty("RELEASE_STORE_PASSWORD").orEmpty())
    .get()
val releaseKeyPassword = providers.gradleProperty("RELEASE_KEY_PASSWORD")
    .orElse(providers.environmentVariable("RELEASE_KEY_PASSWORD"))
    .orElse(localProps.getProperty("RELEASE_KEY_PASSWORD").orEmpty())
    .get()
val releaseKeyAlias = providers.gradleProperty("RELEASE_KEY_ALIAS")
    .orElse(providers.environmentVariable("RELEASE_KEY_ALIAS"))
    .orElse(localProps.getProperty("RELEASE_KEY_ALIAS").orEmpty())
    .get()

kotlin {
    sourceSets {
        commonMain {
            kotlin.srcDir(genDir)
        }
    }
}

tasks.named("compileKotlinMetadata").configure { dependsOn(generateAuthConfig) }
tasks.matching { it.name.startsWith("compile") && it.name.contains("Kotlin") }.configureEach {
    dependsOn(generateAuthConfig)
}
tasks.matching { it.name.startsWith("ksp") && it.name.contains("KotlinAndroid") }.configureEach {
    dependsOn(generateAuthConfig)
}
