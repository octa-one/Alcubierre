plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.compose)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "com.github.octaone.alcubierre.compose.hilt"
}

dependencies {
    implementation(libs.androidx.lifecycle.compose)
    implementation(libs.androidx.viewmodel)
    implementation(libs.hilt.android)
}
