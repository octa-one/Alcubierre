dependencies {
    implementation(project(":buildlogic-util"))
    implementation(project(":buildlogic-kotlin-plugin"))
    compileOnly(libs.gradle.android.tools)
    compileOnly(libs.gradle.ksp)
}

gradlePlugin {
    plugins {
        register("AlcubierreDeeplinkGradleBase") {
            group = "com.github.octaone.alcubierre"
            id = "alcubierre-deeplink-gradle-base"
            implementationClass = "com.github.octaone.alcubierre.gradle.AlcubierreDeeplinkGradleBasePlugin"
        }
    }
}
