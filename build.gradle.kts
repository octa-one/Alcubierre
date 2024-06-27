plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.vk.recompose.checker) apply false
    alias(libs.plugins.binary.validator) apply false
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(libs.gradle.colonist)
    }
}
