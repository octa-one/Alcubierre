package com.github.octaone.alcubierre.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.github.octaone.alcubierre.buildlogic.util.androidIfPresent
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ApplicationBuildLogicPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("buildlogic.kotlin-android")
                apply("buildlogic.android-base")
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = "com.github.octaone.alcubierre_sample"
                    minSdk = 21
                    targetSdk = 35
                    versionCode = 1
                    versionName = "1.0"
                }
            }

            androidIfPresent {
                buildFeatures {
                    viewBinding = true
                }
            }
        }
    }
}
