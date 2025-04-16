plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "space.octaone.alcubierre.fragment"
}

dependencies {
    api(projects.alcubierreCore)

    implementation(libs.androidx.fragment)
}
