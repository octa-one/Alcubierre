plugins {
    alias(libs.plugins.buildlogic.android.library)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "space.octaone.alcubierre"
}

dependencies {
    api(projects.alcubierreBase)

    implementation(libs.kotlin.coroutines)
}
