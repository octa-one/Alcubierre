dependencies {
    implementation(project(":buildlogic-util"))
    compileOnly(libs.gradle.android.tools)
    compileOnly(libs.gradle.kotlin)
    compileOnly(libs.gradle.binary.validator)
}

gradlePlugin {
    plugins {
        create("PublishBuildLogic") {
            id = "buildlogic.publish"
            implementationClass = "com.github.octaone.alcubierre.buildlogic.PublishBuildLogicPlugin"
        }
    }
}