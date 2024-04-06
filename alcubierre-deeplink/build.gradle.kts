plugins {
    id(Conventions.ANDROID_LIBRARY)
    id("com.joom.colonist.android")
}

android {
    namespace = "com.github.octaone.alcubierre.deeplink"
}

dependencies {
    implementation(projects.alcubierre)
    implementation(projects.alcubierreCodegenApi)
    api(projects.alcubierreCondition)

    implementation(libs.colonist.core)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
}
