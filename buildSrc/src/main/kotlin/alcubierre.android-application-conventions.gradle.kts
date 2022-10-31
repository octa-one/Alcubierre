import org.gradle.api.JavaVersion

plugins {
    id("com.android.application")
    id("alcubierre.kotlin-android-conventions")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.github.octaone.alcubierre_sample"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}