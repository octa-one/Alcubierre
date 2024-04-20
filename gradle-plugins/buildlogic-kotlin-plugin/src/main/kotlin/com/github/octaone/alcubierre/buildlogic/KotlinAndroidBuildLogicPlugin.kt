package com.github.octaone.alcubierre.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinAndroidBuildLogicPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kotlin-android")
                apply("buildlogic.kotlin-base")
            }
        }
    }
}