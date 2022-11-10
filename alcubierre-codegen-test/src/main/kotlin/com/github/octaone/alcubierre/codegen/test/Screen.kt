package com.github.octaone.alcubierre.codegen.test

import androidx.fragment.app.Fragment
import com.github.octaone.alcubierre.codegen.type.Deeplink
import com.github.octaone.alcubierre.codegen.type.DeeplinkParam
import com.github.octaone.alcubierre.screen.FragmentScreen
import java.math.BigDecimal
import kotlinx.parcelize.Parcelize

@Parcelize
@Deeplink("scheme://host/path")
object ObjectScreen : FragmentScreen(FeatureFragment::class)

@Parcelize
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
) : FragmentScreen(FeatureFragment::class)

@Parcelize
@Deeplink("scheme://host/path/{mode}")
data class EnumScreen(val mode: Mode) : FragmentScreen(FeatureFragment::class) {
    enum class Mode { A }
}

@Parcelize
@Deeplink("scheme://host/path/{_ID}")
data class CustomNamedScreen(
    @DeeplinkParam(name = "_ID")
    val id: String
) : FragmentScreen(FeatureFragment::class)

@Parcelize
@Deeplink("scheme://host/path/{name}")
data class DefaultParamScreen(
    val name: String,
    val ids: List<Int> = listOf(1)
) : FragmentScreen(FeatureFragment::class)

@Parcelize
@Deeplink("scheme://host?name={name}&lastName={lastName}&code={code}&foo={foo}&bar={bar}")
data class ReflectionScreen(
    val name: String = "defaultName",
    val lastName: String,
    val code: String = "123",
    val foo: String? = "456",
    val bar: String?
) : FragmentScreen(FeatureFragment::class)

@Parcelize
@Deeplink("scheme://host/path/{name}")
data class NullableScreen(
    val name: String?
) : FragmentScreen(FeatureFragment::class)

@Parcelize
@Deeplink(
    "scheme://host/{id}", // внутренний диплинк
    "https://host/{id}", // апплинк с теми же плейсхолдерами

    "scheme://host" // служебный диплинк
)
data class MultiplePatternScreen(
    val id: String? = "default"
) : FragmentScreen(FeatureFragment::class)

class FeatureFragment : Fragment()