import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.samWithReceiver.gradle.SamWithReceiverExtension

plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kotlin.sam) apply false
}

subprojects {
    with(pluginManager) {
        apply("org.jetbrains.kotlin.jvm")
        apply("org.jetbrains.kotlin.plugin.sam.with.receiver")
        apply("java-gradle-plugin")
    }

    extensions.configure<SamWithReceiverExtension> {
        annotation("org.gradle.api.HasImplicitReceiver")
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            // https://docs.gradle.org/current/userguide/kotlin_dsl.html#sec:kotlin_compiler_arguments
            freeCompilerArgs.addAll(
                "-java-parameters",
                "-Xjvm-default=all",
                "-Xjsr305=strict",
                "-Xsam-conversions=class"
            )
        }
    }

    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        "compileOnly"(gradleApi())
        "compileOnly"(gradleKotlinDsl())
        "compileOnly"(rootProject.libs.kotlin.stdlib)
    }
}