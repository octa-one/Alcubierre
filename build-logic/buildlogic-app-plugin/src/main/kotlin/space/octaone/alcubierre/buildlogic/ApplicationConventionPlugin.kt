package space.octaone.alcubierre.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import space.octaone.alcubierre.buildlogic.util.androidIfPresent

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
                    applicationId = "space.octaone.alcubierre_sample"
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
