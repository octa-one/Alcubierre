package com.github.octaone.alcubierre.buildlogic

import com.android.build.api.dsl.LibraryExtension
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
            }

            val libraryExtension = extensions.findByType<LibraryExtension>()
            val componentName: String

            if (libraryExtension != null) {
                componentName = "release"
                with(libraryExtension) {
                    publishing {
                        singleVariant("release") {
                            withSourcesJar()
                        }
                    }
                }
            } else {
                componentName = "java"
            }

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