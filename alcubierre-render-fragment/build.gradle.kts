plugins {
    id(Conventions.ANDROID_LIBRARY)
}

android {
    namespace = "com.github.octaone.alcubierre.fragment"
}

dependencies {
    implementation(libs.androidx.fragment)
    implementation(projects.alcubierre)
}
