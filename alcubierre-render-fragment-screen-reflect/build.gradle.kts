plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "com.github.octaone.alcubierre.fragment.screen.reflect"
}

dependencies {
    api(projects.alcubierreBase)
    api(projects.alcubierreRenderFragment)

    implementation(libs.androidx.fragment)
}
