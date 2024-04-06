plugins {
    id("com.android.application")
    id("alcubierre.kotlin-android-conventions")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.github.octaone.alcubierre_sample"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}