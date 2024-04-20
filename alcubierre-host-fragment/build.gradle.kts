plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "com.github.octaone.alcubierre.fragment.host"
}

dependencies {
    implementation(libs.androidx.fragment)
    implementation(projects.alcubierre)
    implementation(projects.alcubierreRenderFragment)
}
