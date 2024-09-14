package com.github.octaone.alcubierre.buildlogic

import com.github.octaone.alcubierre.buildlogic.util.androidIfPresent
import org.gradle.api.Plugin
import org.gradle.api.Project

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
