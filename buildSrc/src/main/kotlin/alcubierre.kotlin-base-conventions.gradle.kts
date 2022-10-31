import com.android.build.gradle.BaseExtension
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import util.implementation
import util.withVersionCatalog

tasks.withType<KotlinCompile>() {
    kotlinOptions {
        jvmTarget = "11"
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
