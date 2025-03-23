package space.octaone.alcubierre.buildlogic

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import space.octaone.alcubierre.buildlogic.util.androidIfPresent

class AndroidBaseBuildLogicPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            androidIfPresent {
                compileSdk = 35
                defaultConfig {
                    minSdk = 21
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }
        }
    }
}
