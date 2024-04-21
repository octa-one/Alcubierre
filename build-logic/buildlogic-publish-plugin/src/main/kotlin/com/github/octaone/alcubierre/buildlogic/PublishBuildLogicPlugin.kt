package com.github.octaone.alcubierre.buildlogic

import com.android.build.api.dsl.LibraryExtension
import com.github.octaone.alcubierre.buildlogic.util.kotlinIfPresent
import kotlinx.validation.ApiValidationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.get

class PublishBuildLogicPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("maven-publish")
                apply("org.jetbrains.kotlinx.binary-compatibility-validator")
            }

            kotlinIfPresent {
                explicitApi()
            }

            extensions.configure<ApiValidationExtension> {
                nonPublicMarkers.add("com.github.octaone.alcubierre.annotation.AlcubierreInternalApi")

                validationDisabled = false
                apiDumpDirectory = "api"
            }

            val libraryExtension = extensions.findByType<LibraryExtension>()

            if (libraryExtension != null) {
                with(libraryExtension) {
                    publishing {
                        singleVariant("release") {
                            withSourcesJar()
                        }
                    }
                }
            }

            val componentName = if (libraryExtension != null) "release" else "java"
            extensions.configure<PublishingExtension> {
                publications {
                    create<MavenPublication>("release") {
                        groupId = "com.github.octaone.alcubierre"
                        artifactId = project.name
                        version = "0.1"

                        afterEvaluate {
                            from(components[componentName])
                        }
                    }
                }
            }
        }
    }
}