import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugins.KOTLIN)
    id(Plugins.KAPT)
    id(Plugins.LINT)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    compileOnly(libs.lint.api)
    compileOnly(libs.lint.checks)
    compileOnly(libs.autoservice.annotations)
    kapt(libs.autoservice)

    testImplementation(libs.junit)
    testImplementation(libs.lint)
    testImplementation(libs.lint.tests)
}
