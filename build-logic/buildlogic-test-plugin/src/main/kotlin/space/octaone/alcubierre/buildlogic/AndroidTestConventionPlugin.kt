package space.octaone.alcubierre.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import space.octaone.alcubierre.buildlogic.util.androidIfPresent

class AndroidTestBuildLogicPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("buildlogic.kotlin-test")
            }

            androidIfPresent {
                @Suppress("UnstableApiUsage")
                testOptions {
                    unitTests.isReturnDefaultValues = true
                }
            }
        }
    }
}
