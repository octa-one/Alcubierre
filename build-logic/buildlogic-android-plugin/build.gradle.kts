dependencies {
    implementation(project(":buildlogic-util"))
    implementation(project(":buildlogic-kotlin-plugin"))
    compileOnly(libs.gradle.android.tools)
}

gradlePlugin {
    plugins {
        register("AndroidBaseBuildLogic") {
            id = "buildlogic.android-base"
            implementationClass = "space.octaone.alcubierre.buildlogic.AndroidBaseBuildLogicPlugin"
        }
        register("AndroidLibraryBuildLogic") {
            id = "buildlogic.android-library"
            implementationClass = "space.octaone.alcubierre.buildlogic.AndroidLibraryBuildLogicPlugin"
        }
    }
}
