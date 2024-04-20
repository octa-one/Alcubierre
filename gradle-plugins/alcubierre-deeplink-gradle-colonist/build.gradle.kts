group = "com.github.octaone.alcubierre"

dependencies {
    implementation(project(":buildlogic-util"))
    implementation(project(":buildlogic-kotlin-plugin"))
    implementation(project(":alcubierre-deeplink-gradle-base"))
    compileOnly(libs.gradle.android.tools)
    implementation(libs.gradle.colonist)
}

gradlePlugin {
    plugins {
        register("AlcubierreDeeplinkGradleColonist") {
            id = "alcubierre-deeplink-gradle-colonist"
            implementationClass = "com.github.octaone.alcubierre.gradle.AlcubierreDeeplinkGradleColonistPlugin"
        }
    }
}
