plugins {
    id(Conventions.ANDROID_APPLICATION)
    id(Plugins.PARCELIZE)
    id(Conventions.DEEPLINK_CODEGEN)
    id(Plugins.COLONIST)
}

android {
    namespace = "com.github.octaone.alcubierre.sample"
}

dependencies {
    implementation(projects.alcubierre)
    implementation(projects.alcubierreRenderFragment)
    implementation(projects.alcubierreDeeplink)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.core)
    implementation(libs.androidx.fragment)
    implementation(libs.material)
}
