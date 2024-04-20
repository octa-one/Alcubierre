dependencies {
    implementation(project(":buildlogic-util"))
    compileOnly(libs.gradle.android.tools)
}

gradlePlugin {
    plugins {
        create("PublishBuildLogic") {
            id = "buildlogic.publish"
            implementationClass = "com.github.octaone.alcubierre.buildlogic.PublishBuildLogicPlugin"
        }
    }
}