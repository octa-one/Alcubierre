plugins {
    id(Conventions.ANDROID_LIBRARY)
    id(Conventions.PARCELIZE)
    id(Conventions.KSP)
    id(Conventions.ALCUBIERRE_DEEPLINK)
}

android {
    namespace = "com.github.octaone.alcubierre.codegen.test"
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.fragment)

    implementation(projects.alcubierre)
    implementation(projects.alcubierreRenderFragment)
    implementation(projects.alcubierreCodegen)

    testImplementation(libs.junit)
}