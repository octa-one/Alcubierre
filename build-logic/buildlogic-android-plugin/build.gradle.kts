dependencies {
    implementation(project(":buildlogic-util"))
    implementation(project(":buildlogic-kotlin-plugin"))
    compileOnly(libs.gradle.android.tools)
}

gradlePlugin {
    plugins {
        register("AndroidBaseBuildLogic") {
            id = "buildlogic.android-base"
            implementationClass = "com.github.octaone.alcubierre.buildlogic.AndroidBaseBuildLogicPlugin"
        }
        register("AndroidLibraryBuildLogic") {
            id = "buildlogic.android-library"
            implementationClass = "com.github.octaone.alcubierre.buildlogic.AndroidLibraryBuildLogicPlugin"
        }
    }
}
