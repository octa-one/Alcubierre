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

includeBuild("build-logic")

include(":alcubierre")
include(":alcubierre-base")
include(":alcubierre-host-fragment")
include(":alcubierre-render-fragment")
include(":alcubierre-render-fragment-screen-reflect")
include(":alcubierre-render-compose")
include(":alcubierre-render-compose-screen-reflect")
include(":alcubierre-deeplink-processor")
include(":alcubierre-deeplink-processor-api")
include(":alcubierre-deeplink-processor-test")
include(":alcubierre-deeplink")
include(":alcubierre-condition")
include(":alcubierre-condition-reflect")

include(":sample-fragment")
include(":sample-fragment-host-fragment")
include(":sample-fragment-deeplink")
include(":sample-compose")
