package com.github.octaone.alcubierre.buildlogic.util

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestExtension
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.DynamicFeatureAndroidComponentsExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.api.variant.TestAndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

typealias AnyCommonExtension = CommonExtension<*, *, *, *, *, *>
typealias AnyAndroidComponentsExtension = AndroidComponentsExtension<*, *, *>

fun Project.androidIfPresent(configuration: AnyCommonExtension.() -> Unit) {
    val extension = extensions.findByType<LibraryExtension>()
        ?: extensions.findByType<ApplicationExtension>()
        ?: extensions.findByType<TestExtension>()
        ?: extensions.findByType<DynamicFeatureExtension>()
    extension?.configuration()
}

fun Project.androidComponentsIfPresent(configuration: AnyAndroidComponentsExtension.() -> Unit) {
    val extension = extensions.findByType<LibraryAndroidComponentsExtension>()
        ?: extensions.findByType<ApplicationAndroidComponentsExtension>()
        ?: extensions.findByType<TestAndroidComponentsExtension>()
        ?: extensions.findByType<DynamicFeatureAndroidComponentsExtension>()
    extension?.configuration()
}
