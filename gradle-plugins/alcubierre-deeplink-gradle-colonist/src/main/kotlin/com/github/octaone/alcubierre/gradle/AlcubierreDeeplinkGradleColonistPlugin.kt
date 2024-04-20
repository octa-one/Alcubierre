package com.github.octaone.alcubierre.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class AlcubierreDeeplinkGradleColonistPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("alcubierre-deeplink-gradle-base")
                apply("com.joom.colonist.android")
            }
        }
    }
}
