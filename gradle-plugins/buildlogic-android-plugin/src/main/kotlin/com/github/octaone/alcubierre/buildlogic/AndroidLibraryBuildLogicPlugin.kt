package com.github.octaone.alcubierre.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryBuildLogicPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("buildlogic.kotlin-android")
                apply("buildlogic.android-base")
            }
        }
    }
}
