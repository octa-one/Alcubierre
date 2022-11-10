package com.github.octaone.alcubierre.sample.screen

import com.github.octaone.alcubierre.codegen.api.Deeplink
import com.github.octaone.alcubierre.codegen.api.DeeplinkParam
import com.github.octaone.alcubierre.sample.fragment.SampleFragment
import com.github.octaone.alcubierre.screen.FragmentScreen
import kotlinx.parcelize.Parcelize

@Parcelize
@Deeplink("myapp://sample?id={id}")
data class SampleScreen(
    @DeeplinkParam(name = "id") val someId: Int
) : FragmentScreen(SampleFragment::class)
