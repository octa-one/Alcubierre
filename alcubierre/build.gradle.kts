plugins {
    id(Conventions.ANDROID_LIBRARY)
    id(Plugins.PARCELIZE)
}

android {
    namespace = "com.github.octaone.alcubierre"
}

dependencies {

    lintPublish(projects.alcubierreLint)
}
