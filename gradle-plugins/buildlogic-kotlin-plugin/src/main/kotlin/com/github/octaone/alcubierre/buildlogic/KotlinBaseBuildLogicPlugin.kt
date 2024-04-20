package com.github.octaone.alcubierre.buildlogic

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class KotlinBaseBuildLogicPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions {
                    jvmTarget = JavaVersion.VERSION_17.toString()
                    freeCompilerArgs += listOf(
                        "-opt-in=kotlin.RequiresOptIn",
                        "-opt-in=kotlin.contracts.ExperimentalContracts"
                    )
                }
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                "implementation"(libs.findLibrary("androidx.annotation").get())
                "implementation"(libs.findLibrary("kotlin.stdlib").get())
            }
        }
    }
}
