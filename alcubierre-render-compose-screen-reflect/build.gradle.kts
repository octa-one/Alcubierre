plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.compose)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "com.github.octaone.alcubierre.compose.screen.reflect"

    defaultConfig {
        consumerProguardFiles("proguard/rules.pro")
    }
}

dependencies {
    api(projects.alcubierreBase)
    api(projects.alcubierreRenderCompose)

    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.compose)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.viewmodel)
    implementation(libs.androidx.viewmodel.savedstate)
    implementation(libs.androidx.savedstate)
}
