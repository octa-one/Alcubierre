plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.android.test)
    alias(libs.plugins.alcubierre.deeplink.colonist)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
    id(libs.plugins.ksp.get().pluginId)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "com.github.octaone.alcubierre.codegen.test"
}

ksp {
    arg("alcubierre.allowedTypes", "com.github.octaone.alcubierre.codegen.test.TestScreen")
}

dependencies {
    implementation(projects.alcubierre)
    implementation(projects.alcubierreCodegenApi)

    ksp(projects.alcubierreCodegen)

    implementation(libs.junit)
    implementation(libs.kotlin.test)
}
