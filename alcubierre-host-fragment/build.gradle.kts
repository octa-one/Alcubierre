plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "space.octaone.alcubierre.fragment.host"
}

dependencies {
    api(projects.alcubierre)
    api(projects.alcubierreRenderFragment)

    implementation(libs.androidx.fragment)
}
