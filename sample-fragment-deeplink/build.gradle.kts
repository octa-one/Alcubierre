plugins {
    alias(libs.plugins.buildlogic.android.app)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
    alias(libs.plugins.alcubierre.deeplink.colonist)
}

android {
    namespace = "com.github.octaone.alcubierre.sample"
}

dependencies {
    implementation(projects.alcubierre)
    implementation(projects.alcubierreRenderFragment)
    implementation(projects.alcubierreDeeplink)
    implementation(projects.alcubierreCondition)

    implementation(libs.kotlin.coroutines.android)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.core)
    implementation(libs.androidx.fragment)
    implementation(libs.material)
}
