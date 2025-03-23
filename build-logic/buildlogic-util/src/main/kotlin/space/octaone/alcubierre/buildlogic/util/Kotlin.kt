package space.octaone.alcubierre.buildlogic.util

import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

fun Project.kotlinIfPresent(configuration: KotlinProjectExtension.() -> Unit) {
    val extension = extensions.findByType<KotlinJvmProjectExtension>()
        ?: extensions.findByType<KotlinAndroidProjectExtension>()
    extension?.configuration()
}
