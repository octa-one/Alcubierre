plugins {
    id(Conventions.ANDROID_APPLICATION)
    id(Plugins.PARCELIZE)
}

android {
    namespace = "com.github.octaone.alcubierre.sample"
}

dependencies {
    implementation(projects.alcubierre)
    implementation(projects.alcubierreHostFragment)
    implementation(projects.alcubierreRenderFragment)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.core)
    implementation(libs.androidx.fragment)
    implementation(libs.material)
}
