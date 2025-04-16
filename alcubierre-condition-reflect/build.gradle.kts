plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "space.octaone.alcubierre.condition.reflect"
}

dependencies {
    api(projects.alcubierreCore)
    api(projects.alcubierreCondition)
}
