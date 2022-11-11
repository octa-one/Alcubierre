import util.commonAndroid
import util.implementation
import util.withVersionCatalog

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