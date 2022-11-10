plugins {
    id(Conventions.KOTLIN_LIBRARY)
}

dependencies {
    implementation(projects.alcubierreCodegenApi)

    implementation(libs.androidx.annotation)
    implementation(libs.ksp.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
}
