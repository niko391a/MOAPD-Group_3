plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "dk.itu.moapd.x9.mnla_nals"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "dk.itu.moapd.x9.mnla_nals"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}
    ktlint {
        android.set(true)
        outputToConsole.set(true)
        ignoreFailures.set(false)

        filter {
            exclude("**/build/**")
            exclude("**/generated/**")
        }
    }

    detekt {
        toolVersion = libs.versions.detekt.get()

        buildUponDefaultConfig = true
        allRules = false
        parallel = false

        config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
        baseline = file("$rootDir/config/detekt/baseline.xml")
    }

    tasks.named("check") {
        dependsOn("ktlintCheck")
    }

    tasks.register("fix") {
        group = "verification"
        description = "Auto-fix formatting issues (ktlint)."
        dependsOn("ktlintFormat", "ktlintKotlinScriptFormat")
    }

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.runtime.saveable)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.compose.ui.tooling)
}