plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "com.github.octaone.alcubierre.fragment"
}

dependencies {
    implementation(libs.androidx.fragment)
    implementation(projects.alcubierreBase)
}
