plugins {
    id(Conventions.ANDROID_LIBRARY)
    id(Conventions.COMPOSE)
}

android {
    namespace = "com.github.octaone.alcubierre.compose"
}

dependencies {
    implementation(projects.alcubierre)

    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.compose)
    implementation(libs.androidx.viewmodel)
    implementation(libs.androidx.viewmodel.savedstate)
    implementation(libs.androidx.savedstate)
}
