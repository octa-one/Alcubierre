plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "com.github.octaone.alcubierre.condition.reflect"
}

dependencies {
    implementation(projects.alcubierre)
    implementation(projects.alcubierreCondition)
}
