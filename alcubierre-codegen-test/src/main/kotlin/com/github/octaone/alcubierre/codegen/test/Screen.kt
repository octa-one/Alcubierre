@file:SuppressLint("ParcelCreator")

package com.github.octaone.alcubierre.codegen.test

import android.annotation.SuppressLint
import com.github.octaone.alcubierre.codegen.api.Deeplink
import com.github.octaone.alcubierre.codegen.api.DeeplinkParam
import java.math.BigDecimal

@Deeplink("scheme://host/path")
object ObjectScreen : TestScreen()

@Deeplink("scheme://host/path/{a}/{b}/{c}/{d}/{e}/{f}/{g}/{h}/{i}")
data class BasicConversionScreen(
    val a: String,
    val b: Int,
    val c: Byte,
    val d: Long,
    val e: Short,
    val f: Float,
    val g: Double,
    val h: Boolean,
    val i: BigDecimal
) : TestScreen()

@Deeplink("scheme://host/path/{mode}")
data class EnumScreen(val mode: Mode) : TestScreen() {
    enum class Mode { A }
}

@Deeplink("scheme://host/path/{_ID}")
data class CustomNamedScreen(
    @DeeplinkParam(name = "_ID")
    val id: String
) : TestScreen()

@Deeplink("scheme://host/path/{name}/{b}/{c}/{d}/{e}/{f}/{g}/{h}")
data class DefaultParamScreen(
    val name: String,
    val ids: List<Int> = listOf(1),
    val b: Int = 1,
    val c: Byte = 2,
    val d: Long = 3,
    val e: Short = 4,
    val f: Float = 5f,
    val g: Double = 6.0,
    val h: Boolean = false
) : TestScreen()

@Deeplink("scheme://host?name={name}&lastName={lastName}&code={code}&foo={foo}&bar={bar}")
data class ReflectionScreen(
    val name: String = "defaultName",
    val lastName: String,
    val code: String = "123",
    val foo: String? = "456",
    val bar: String?
) : TestScreen()

@Deeplink("scheme://host/path/{a}/{b}/{c}/{d}/{e}/{f}/{g}/{h}")
data class NullableScreen(
    val a: String?,
    val b: Int?,
    val c: Byte?,
    val d: Long?,
    val e: Short?,
    val f: Float?,
    val g: Double?,
    val h: Boolean?
) : TestScreen()

@Deeplink("scheme://host/path/{a}/{b}/{c}/{d}/{e}/{f}/{g}/{h}")
data class NullableWithDefaultScreen(
    val a: String? = "default",
    val b: Int? = 1,
    val c: Byte? = 2,
    val d: Long? = 3,
    val e: Short? = 4,
    val f: Float? = 5f,
    val g: Double? = 6.0,
    val h: Boolean? = false
) : TestScreen()

@Deeplink(
    "scheme://host/{id}", // внутренний диплинк
    "https://host/{id}", // апплинк с теми же плейсхолдерами

    "scheme://host" // служебный диплинк
)
data class MultiplePatternScreen(
    val id: String? = "default"
) : TestScreen()
