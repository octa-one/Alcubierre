dependencies {
    implementation(project(":buildlogic-util"))
    compileOnly(libs.gradle.android.tools)
    compileOnly(libs.gradle.kotlin)
}

gradlePlugin {
    plugins {
        register("KotlinAndroidBuildLogic") {
            id = "buildlogic.kotlin-android"
            implementationClass = "com.github.octaone.alcubierre.buildlogic.KotlinAndroidBuildLogicPlugin"
        }
        register("KotlinJvmBuildLogic") {
            id = "buildlogic.kotlin-jvm"
            implementationClass = "com.github.octaone.alcubierre.buildlogic.KotlinJvmBuildLogicPlugin"
        }
        register("KotlinBaseBuildLogic") {
            id = "buildlogic.kotlin-base"
            implementationClass = "com.github.octaone.alcubierre.buildlogic.KotlinBaseBuildLogicPlugin"
        }
    }
}