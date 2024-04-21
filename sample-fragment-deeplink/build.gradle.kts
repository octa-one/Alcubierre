plugins {
    alias(libs.plugins.buildlogic.android.app)
    id(libs.plugins.ksp.get().pluginId)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
    id(libs.plugins.colonist.get().pluginId)
}

android {
    namespace = "com.github.octaone.alcubierre.sample"
}

ksp {
    arg("alcubierre.registryBaseName", project.path)
}

dependencies {
    implementation(projects.alcubierre)
    implementation(projects.alcubierreRenderFragment)
    implementation(projects.alcubierreDeeplink)
    implementation(projects.alcubierreCondition)
    ksp(projects.alcubierreDeeplinkProcessor)

    implementation(libs.kotlin.coroutines.android)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.core)
    implementation(libs.androidx.fragment)
    implementation(libs.material)
}
