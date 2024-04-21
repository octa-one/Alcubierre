plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.publish)
}

android {
    namespace = "com.github.octaone.alcubierre.condition"

    defaultConfig {
        consumerProguardFiles("proguard/rules.pro")
    }
}

dependencies {
    implementation(projects.alcubierre)
}
