import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import util.commonAndroid
import util.implementation
import util.withVersionCatalog

tasks.withType<KotlinCompile> {
    kotlinOptions {
        if (project.findProperty("enableComposeCompilerReports") == "true") {
            freeCompilerArgs += listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                        project.buildDir.absolutePath + "/compose_metrics"
            )
            freeCompilerArgs += listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                        project.buildDir.absolutePath + "/compose_metrics"
            )
        }
    }
}

commonAndroid {
    buildFeatures {
        compose = true
    }
    composeOptions {
        withVersionCatalog { libs ->
            kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
        }
    }
}

dependencies {
    withVersionCatalog { libs ->
        implementation(platform(libs.compose.bom))
        implementation(libs.bundles.compose)
    }
}