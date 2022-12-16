plugins {
    id(Conventions.ANDROID_LIBRARY)
    id(Conventions.COMPOSE)
}

android {
    namespace = "com.github.octaone.alcubierre.compose"
}

dependencies {
    implementation(projects.alcubierre)
}
