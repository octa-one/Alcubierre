dependencies {
    implementation(project(":buildlogic-util"))
    compileOnly(libs.gradle.android.tools)
    compileOnly(libs.gradle.kotlin)
}

gradlePlugin {
    plugins {
        register("KotlinAndroidBuildLogic") {
            id = "buildlogic.kotlin-android"
            implementationClass = "space.octaone.alcubierre.buildlogic.KotlinAndroidBuildLogicPlugin"
        }
        register("KotlinJvmBuildLogic") {
            id = "buildlogic.kotlin-jvm"
            implementationClass = "space.octaone.alcubierre.buildlogic.KotlinJvmBuildLogicPlugin"
        }
        register("KotlinBaseBuildLogic") {
            id = "buildlogic.kotlin-base"
            implementationClass = "space.octaone.alcubierre.buildlogic.KotlinBaseBuildLogicPlugin"
        }
    }
}