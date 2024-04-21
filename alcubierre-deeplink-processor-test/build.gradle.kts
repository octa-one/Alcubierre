plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.android.test)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
    id(libs.plugins.ksp.get().pluginId)
}

android {
    namespace = "com.github.octaone.alcubierre.deeplink.processor.test"
}

ksp {
    arg("alcubierre.registryBaseName", project.path)
    arg("alcubierre.allowedTypes", "com.github.octaone.alcubierre.deeplink.test.TestScreen")
}

dependencies {
    implementation(projects.alcubierre)
    implementation(projects.alcubierreDeeplink)
    ksp(projects.alcubierreDeeplinkProcessor)

    implementation(libs.junit)
    implementation(libs.kotlin.test)
}
