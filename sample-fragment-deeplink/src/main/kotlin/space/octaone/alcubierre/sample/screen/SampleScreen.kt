package space.octaone.alcubierre.sample.screen

import kotlinx.parcelize.Parcelize
import space.octaone.alcubierre.deeplink.processor.api.Deeplink
import space.octaone.alcubierre.deeplink.processor.api.DeeplinkParam
import space.octaone.alcubierre.sample.fragment.SampleFragment
import space.octaone.alcubierre.screen.FragmentScreen

@Parcelize
@Deeplink("myapp://sample?id={id}")
data class SampleScreen(
    @DeeplinkParam(name = "id") val someId: Int
) : FragmentScreen(SampleFragment::class)
