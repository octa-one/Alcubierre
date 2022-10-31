enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "alcubierre-root"
include(":sample-alcubierre-host-fragment")
include(":sample-alcubierre-standalone")

include(":alcubierre")
include(":alcubierre-host-fragment")
include(":alcubierre-render-fragment")
include(":alcubierre-render-compose")
