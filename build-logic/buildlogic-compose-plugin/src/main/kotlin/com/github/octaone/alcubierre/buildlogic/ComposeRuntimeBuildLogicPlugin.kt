package com.github.octaone.alcubierre.buildlogic

import com.github.octaone.alcubierre.buildlogic.util.androidIfPresent
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class ComposeRuntimeBuildLogicPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            androidIfPresent {
                buildFeatures {
                    compose = true
                }
                composeOptions {
                    kotlinCompilerExtensionVersion = libs.findVersion("compose.compiler").get().toString()
                }
            }
            tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions {
                    freeCompilerArgs += listOf(
                        "-P",
                        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${target.buildDir}/compose-metrics/",
                        "-P",
                        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${target.buildDir}/compose-reports/"
                    )
                }
            }

            dependencies {
                "implementation"(platform(libs.findLibrary("compose.bom").get()))
                "implementation"(libs.findLibrary("compose.runtime").get())
            }
        }
    }
}