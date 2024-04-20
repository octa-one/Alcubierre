dependencies {
    implementation(project(":buildlogic-util"))

    compileOnly(libs.gradle.android.tools)
}

gradlePlugin {
    plugins {
        register("AndroidTestBuildLogic") {
            id = "buildlogic.android-test"
            implementationClass = "com.github.octaone.alcubierre.buildlogic.AndroidTestBuildLogicPlugin"
        }
        register("KotlinTestBuildLogic") {
            id = "buildlogic.kotlin-test"
            implementationClass = "com.github.octaone.alcubierre.buildlogic.KotlinTestBuildLogicPlugin"
        }
    }
}