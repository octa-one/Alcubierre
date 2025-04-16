plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.android.test)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "space.octaone.alcubierre.deeplink"
}

dependencies {
    api(projects.alcubierreCore)
    api(projects.alcubierreDeeplinkProcessorApi)
    implementation(projects.alcubierre)
    implementation(projects.alcubierreCondition)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
}
