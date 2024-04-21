dependencies {
    implementation(project(":buildlogic-util"))
    compileOnly(libs.gradle.android.tools)
    compileOnly(libs.gradle.kotlin)
}

gradlePlugin {
    plugins {
        register("ComposeBuildLogic") {
            id = "buildlogic.compose"
            implementationClass = "com.github.octaone.alcubierre.buildlogic.ComposeBuildLogicPlugin"
        }
        register("ComposeRuntimeBuildLogic") {
            id = "buildlogic.compose-runtime"
            implementationClass = "com.github.octaone.alcubierre.buildlogic.ComposeRuntimeBuildLogicPlugin"
        }
    }
}