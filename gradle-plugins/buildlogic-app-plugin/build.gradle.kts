dependencies {
    implementation(project(":buildlogic-util"))
    implementation(project(":buildlogic-android-plugin"))
    implementation(project(":buildlogic-kotlin-plugin"))
    implementation(project(":buildlogic-test-plugin"))
    compileOnly(libs.gradle.android.tools)
}

gradlePlugin {
    plugins {
        create("ApplicationBuildLogic") {
            id = "buildlogic.android-app"
            implementationClass = "com.github.octaone.alcubierre.buildlogic.ApplicationBuildLogicPlugin"
        }
    }
}