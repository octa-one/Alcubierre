plugins {
    id(Conventions.ANDROID_LIBRARY)
}

android {
    namespace = "com.github.octaone.alcubierre.fragment.host"
}

dependencies {
    implementation(libs.androidx.fragment)
    implementation(projects.alcubierre)
    implementation(projects.alcubierreRenderFragment)
}
