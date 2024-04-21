plugins {
    alias(libs.plugins.buildlogic.kotlin.library)
    alias(libs.plugins.buildlogic.publish)
}

dependencies {
    implementation(projects.alcubierreDeeplinkProcessorApi)

    implementation(libs.androidx.annotation)
    implementation(libs.ksp.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
}
