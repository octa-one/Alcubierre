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

includeBuild("gradle-plugins")

include(":alcubierre")
include(":alcubierre-base")
include(":alcubierre-host-fragment")
include(":alcubierre-render-fragment")
include(":alcubierre-render-compose")
include(":alcubierre-codegen")
include(":alcubierre-codegen-api")
include(":alcubierre-codegen-test")
include(":alcubierre-deeplink")
include(":alcubierre-condition")

include(":sample-fragment")
include(":sample-fragment-host-fragment")
include(":sample-fragment-deeplink")
include(":sample-compose")
