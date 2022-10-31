package util

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.findPlugin
import org.gradle.kotlin.dsl.typeOf

typealias AnyCommonExtension = CommonExtension<*, *, *, *>

fun Project.commonAndroid(configuration: AnyCommonExtension.() -> Unit) {
    val extension = extensions.findByType<LibraryExtension>() ?: extensions.findByType<BaseAppModuleExtension>()
    requireNotNull(extension) { "Extensions of type 'LibraryExtension' or 'BaseAppModuleExtension' do not exist" }
    extension.configuration()
}
