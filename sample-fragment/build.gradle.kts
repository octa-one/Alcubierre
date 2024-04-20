plugins {
    alias(libs.plugins.buildlogic.android.app)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
}

android {
    namespace = "com.github.octaone.alcubierre.sample"
}

dependencies {
    implementation(projects.alcubierre)
    implementation(projects.alcubierreRenderFragment)

    implementation(libs.kotlin.coroutines.android)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.core)
    implementation(libs.androidx.fragment)
    implementation(libs.material)
}
