package util

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

typealias AnyCommonExtension = CommonExtension<*, *, *, *, *>

fun Project.commonAndroid(configuration: AnyCommonExtension.() -> Unit) {
    val extension = extensions.findByType<LibraryExtension>() ?: extensions.findByType<BaseAppModuleExtension>()
    requireNotNull(extension) { "Extensions of type 'LibraryExtension' or 'BaseAppModuleExtension' do not exist" }
    extension.configuration()
}
