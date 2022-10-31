package util

import org.gradle.api.Project
import org.gradle.kotlin.dsl.the
import org.gradle.accessors.dm.LibrariesForLibs

fun Project.withVersionCatalog(block: (libs: LibrariesForLibs) -> Unit) {
    if (project.name != "gradle-kotlin-dsl-accessors") {
        block.invoke(the<LibrariesForLibs>())
    }
}