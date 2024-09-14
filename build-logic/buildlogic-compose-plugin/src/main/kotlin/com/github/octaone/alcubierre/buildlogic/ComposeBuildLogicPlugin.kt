package com.github.octaone.alcubierre.buildlogic
import com.github.octaone.alcubierre.buildlogic.util.androidIfPresent
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class ComposeBuildLogicPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("com.vk.composable-skippability-checker")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            androidIfPresent {
                buildFeatures {
                    compose = true
                }
            }
            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    freeCompilerArgs.addAll(
                        "-opt-in=androidx.compose.material.ExperimentalMaterialApi,androidx.compose.material3.ExperimentalMaterial3Api",
                        "-P",
                        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${layout.buildDirectory.get()}/compose-metrics/",
                        "-P",
                        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${layout.buildDirectory.get()}/compose-reports/"
                    )
                }
            }

            dependencies {
                "implementation"(platform(libs.findLibrary("compose.bom").get()))
                "implementation"(libs.findBundle("compose").get())
                "debugImplementation"(libs.findLibrary("compose.ui.tooling").get())
            }
        }
    }
}
