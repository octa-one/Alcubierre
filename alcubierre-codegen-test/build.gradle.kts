plugins {
    id(Conventions.ANDROID_LIBRARY)
    id(Conventions.PARCELIZE)
    id(Conventions.KSP)
    id(Conventions.DEEPLINK_CODEGEN)
}

android {
    namespace = "com.github.octaone.alcubierre.codegen.test"
}

dependencies {
    implementation(projects.alcubierre)
    implementation(projects.alcubierreCodegenApi)

    ksp(projects.alcubierreCodegen)

    implementation(libs.junit)
    implementation(libs.kotlin.test)
}
