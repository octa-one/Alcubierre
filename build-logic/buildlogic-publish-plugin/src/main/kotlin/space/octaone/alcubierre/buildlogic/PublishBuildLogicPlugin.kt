package space.octaone.alcubierre.buildlogic

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import kotlinx.validation.ApiValidationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import space.octaone.alcubierre.buildlogic.util.kotlinIfPresent

class PublishBuildLogicPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.vanniktech.maven.publish")
                apply("org.jetbrains.kotlinx.binary-compatibility-validator")
            }

            kotlinIfPresent {
                explicitApi()
            }

            extensions.configure<ApiValidationExtension> {
                nonPublicMarkers.addAll(
                    listOf(
                        "space.octaone.alcubierre.annotation.AlcubierreInternalApi",
                        "space.octaone.alcubierre.condition.annotation.AlcubierreConditionalNameConstructor",
                        "space.octaone.alcubierre.annotation.AlcubierreFragmentNameConstructor"
                    )
                )

                validationDisabled = false
                apiDumpDirectory = "api"
            }

            extensions.configure<MavenPublishBaseExtension> {
                coordinates("space.octaone.alcubierre", project.name, "0.1.1")

                signAllPublications()

                pom {
                    name.set("Alcubierre")
                    description.set("Alcubierre: An Android Navigation library")
                    url.set("https://github.com/octa-one/Alcubierre")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            distribution.set("repo")
                        }
                    }

                    scm {
                        url.set("https://github.com/octa-one/Alcubierre")
                        connection.set("scm:git:git://github.com/octa-one/Alcubierre.git")
                        developerConnection.set("scm:git:ssh://git@github.com/octa-one/Alcubierre.git")
                    }

                    developers {
                        developer {
                            id.set("octa-one")
                            name.set("Alexander Mitropolsky")
                            email.set("aam.dev@fastmail.com")
                        }
                    }
                }
            }
        }
    }
}