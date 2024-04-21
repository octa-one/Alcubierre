plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.android.test)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "com.github.octaone.alcubierre.deeplink"

    defaultConfig {
        consumerProguardFiles("proguard/rules.pro")
    }
}

dependencies {
    implementation(projects.alcubierre)
    api(projects.alcubierreDeeplinkProcessorApi)
    implementation(projects.alcubierreCondition)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
}
