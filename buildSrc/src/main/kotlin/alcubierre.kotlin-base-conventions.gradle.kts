import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import util.implementation
import util.withVersionCatalog

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs += listOf(
            "-opt-in=kotlin.RequiresOptIn"
        )
    }
}

dependencies {
    withVersionCatalog { libs ->
        implementation(libs.androidx.annotation)
        implementation(libs.kotlin.stdlib)
    }
}
