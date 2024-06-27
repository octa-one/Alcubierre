package com.github.octaone.alcubierre.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class KotlinBaseBuildLogicPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                    freeCompilerArgs.addAll(
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
