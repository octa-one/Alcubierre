import util.commonAndroid
import util.implementation

description = "Convention плагин для работы с диплинками в android модулях. Подключает кодогененрацию к модулю"

plugins {
    id("alcubierre.ksp-conventions")
}

commonAndroid {
    sourceSets {
        listOf("debug", "release").forEach { variant ->
            getByName(variant) {
                java.srcDir("build/generated/ksp/$variant/kotlin")
            }
        }
    }
}

ksp {
    arg("deeplink.registry.base.name", project.path)
}

dependencies {
    implementation(project(":alcubierre-codegen-api"))

    add("ksp", project(":alcubierre-codegen"))
}
