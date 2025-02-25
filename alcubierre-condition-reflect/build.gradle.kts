plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "com.github.octaone.alcubierre.condition.reflect"
}

dependencies {
    api(projects.alcubierreBase)
    api(projects.alcubierreCondition)
}
