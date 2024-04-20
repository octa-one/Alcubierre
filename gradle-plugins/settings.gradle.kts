rootProject.buildFileName = "build.gradle.kts"
rootProject.name = "gradle-plugins"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":alcubierre-deeplink-gradle-base")
include(":alcubierre-deeplink-gradle-colonist")
include(":buildlogic-android-plugin")
include(":buildlogic-app-plugin")
include(":buildlogic-build-check-plugin")
include(":buildlogic-compose-plugin")
include(":buildlogic-kotlin-plugin")
include(":buildlogic-test-plugin")
include(":buildlogic-publish")
include(":buildlogic-util")