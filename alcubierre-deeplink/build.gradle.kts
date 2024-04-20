plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.android.test)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "com.github.octaone.alcubierre.deeplink"
}

dependencies {
    implementation(projects.alcubierre)
    implementation(projects.alcubierreCodegenApi)
    api(projects.alcubierreCondition)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
}
