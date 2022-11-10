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
include(":alcubierre")
include(":alcubierre-host-fragment")
include(":alcubierre-render-fragment")
include(":alcubierre-codegen-base")
include(":alcubierre-codegen")
include(":alcubierre-codegen-test")
include(":alcubierre-deeplink")

include(":sample-fragment")
include(":sample-fragment-host-fragment")
