plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "space.octaone.alcubierre.fragment.screen.reflect"
}

dependencies {
    api(projects.alcubierreCore)
    api(projects.alcubierreRenderFragment)

    implementation(libs.androidx.fragment)
}
