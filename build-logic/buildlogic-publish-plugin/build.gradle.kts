dependencies {
    implementation(project(":buildlogic-util"))
    compileOnly(libs.gradle.android.tools)
    compileOnly(libs.gradle.kotlin)
    compileOnly(libs.gradle.binary.validator)
    compileOnly(libs.gradle.publish)
}

gradlePlugin {
    plugins {
        create("PublishBuildLogic") {
            id = "buildlogic.publish"
            implementationClass = "space.octaone.alcubierre.buildlogic.PublishBuildLogicPlugin"
        }
    }
}