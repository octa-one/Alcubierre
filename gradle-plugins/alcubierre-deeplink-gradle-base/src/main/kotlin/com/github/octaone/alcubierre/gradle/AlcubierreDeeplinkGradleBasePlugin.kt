package com.github.octaone.alcubierre.gradle

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class AlcubierreDeeplinkGradleBasePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
            }

            extensions.configure<KspExtension> {
                arg("alcubierre.registryBaseName", project.path)
            }

            dependencies {
                "implementation"(project(":alcubierre-codegen-api"))
                "ksp"(project(":alcubierre-codegen"))
            }
        }
    }
}
