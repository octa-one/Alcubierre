plugins {
    id(Conventions.KOTLIN_LIBRARY)
    id(Conventions.KSP)
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(libs.ksp.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
}